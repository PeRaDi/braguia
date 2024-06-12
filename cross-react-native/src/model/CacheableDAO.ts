import {database} from '@model/database.ts';
import {Collection, Model} from '@nozbe/watermelondb';

export abstract class CacheableDAO<T extends Model> {
  protected constructor(readonly table: string) {}

  protected abstract fetchListFromRemote(): Promise<any[]>;

  protected abstract fetchOneFromRemote(id: string): Promise<any>;

  protected abstract mapFromRemote(model: T, data: any): void;

  async findById(id: string): Promise<T | null> {
    try {
      const model = (await database.get(this.table).find(id)) as T; // If the record cannot be found, the Promise will be rejected
      return model;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  protected async afterModelSet(model: T, data: any): Promise<void> {}

  protected async fetchListFromDB(): Promise<T[]> {
    try {
      const collection = database.collections.get(this.table) as Collection<T>;
      return await collection.query().fetch();
    } catch (error) {
      console.error(error);
      return [];
    }
  }

  async fetchList(
    params: {fromCache: boolean} = {fromCache: false},
  ): Promise<T[]> {
    try {
      if (params.fromCache) {
        const models = await this.fetchListFromDB();
        if (models.length > 0) {
          return models;
        }
      }

      const dataList = await this.fetchListFromRemote();

      for (let data of dataList) {
        const id = data.id.toString();
        const collection = database.collections.get(
          this.table,
        ) as Collection<T>;
        await database.write(async () => {
          const currentModel = await this.findById(id);
          if (!currentModel) {
            await collection.create(model => {
              model._raw.id = id;
              this.mapFromRemote(model, data);
            });
            // await this.onCreate(collection, id, data);
          } else {
            await currentModel.update(model => this.mapFromRemote(model, data));
            // await this.onUpdate(currentModel, data);
          }
        });
        const model = (await this.findById(id))!;
        await this.afterModelSet(model, data);
      }
    } catch (error) {
      console.error(error);
    }
    return await this.fetchListFromDB();
  }

  async fetchOne(
    id: string,
    params: {fromCache: boolean} = {fromCache: false},
  ): Promise<T | null> {
    try {
      if (params.fromCache) {
        const model = await this.findById(id);
        if (model) {
          return model;
        }
      }

      const data = await this.fetchOneFromRemote(id);

      await database.write(async () => {
        const currentModel = await this.findById(id);
        if (!currentModel) {
          const collection = database.collections.get(
            this.table,
          ) as Collection<T>;
          await collection.create(model => {
            model._raw.id = data.id.toString();
            this.mapFromRemote(model, data);
          });
        } else {
          await currentModel.update(model => this.mapFromRemote(model, data));
        }
      });
    } catch (error) {
      console.error(error);
    }
    return await this.findById(id);
  }
}
