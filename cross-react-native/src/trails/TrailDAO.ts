import {Trail} from '@model/models.ts';
import {CacheableDAO} from '@model/CacheableDAO.ts';
import axiosInstance from "@src/network/axios.config.ts";

export class TrailDAO extends CacheableDAO<Trail> {
  constructor() {
    super(Trail.table);
  }

  protected async fetchListFromRemote(): Promise<any[]> {
    const response = await axiosInstance.get('/trails');
    return response.data as any[];
  }

  protected async fetchOneFromRemote(id: string): Promise<any> {
    const response = await axiosInstance.get(`/trail/${id}`);
    return response.data;
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
