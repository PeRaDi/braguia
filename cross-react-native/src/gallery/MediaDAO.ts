import {Media, Pin} from '@model/models.ts';
import {CacheableDAO} from '@model/CacheableDAO.ts';
import axiosInstance from '@src/network/axios.config.ts';
import {database} from "@model/database.ts";

export class MediaDAO extends CacheableDAO<Media> {
  constructor() {
    super(Media.table);
  }

  protected async fetchListFromRemote(): Promise<any[]> {
    const response = await axiosInstance.get('/content');
    return response.data as any[];
  }

  protected async fetchOneFromRemote(mediaPath: string): Promise<any> {
    const response = await axiosInstance.get(`/media/${mediaPath}`);
    return response.data;
  }

  protected mapFromRemote(model: Media, data: any): void {
    model.fileUrl = data.media_file;
    model.type = data.media_type;
  }

  protected async afterModelSet(model: Media, data: any): Promise<void> {
    await database.write(async () => {
      const id = data.media_pin.toString();
      const pin = await database.collections.get<Pin>(Pin.table).find(id);
      await model.update((m) => {
        m.pin.set(pin);
      });
    });
  }
}

export const mediaDAO = new MediaDAO();
