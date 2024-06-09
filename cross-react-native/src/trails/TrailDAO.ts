import {Trail} from '@model/models.ts';
import {apiURL} from 'app.json';
import {CacheableDAO} from '@model/CacheableDAO.ts';

export class TrailDAO extends CacheableDAO<Trail> {
  constructor() {
    super(Trail.table);
  }

  protected async fetchListFromRemote(): Promise<any[]> {
    const response = await fetch(`${apiURL}/trails`);
    return (await response.json()) as any[];
  }

  protected async fetchOneFromRemote(id: string): Promise<any> {
    const response = await fetch(`${apiURL}/trail/${id}`);
    return await response.json();
  }

  protected mapFromRemote(model: Trail, data: any): void {
    model.name = data.trail_name;
    model.description = data.trail_desc;
    model.imageUrl = data.trail_img;
    model.duration = data.trail_duration;
    model.difficulty = data.trail_difficulty;
  }
}

export const trailDAO = new TrailDAO();
