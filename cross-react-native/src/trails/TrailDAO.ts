import {Pin, Trail} from '@model/models.ts';
import {CacheableDAO} from '@model/CacheableDAO.ts';
import axiosInstance from '@src/network/axios.config.ts';

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

  protected async afterModelSet(model: Trail, data: any): Promise<void> {
    const edges = await model.setEdges(data.edges);

    for (const d of data.edges ?? []) {
      const edge = edges.find(e => e.id === d.id.toString());
      if (edge) {
        const {start, start_data, end, end_data} = await edge.setStartEndPins(
          d,
        );
        await this.updatePin(start, start_data);
        await this.updatePin(end, end_data);
      }
    }

    await model.setRelTrails(data.rel_trail);
  }

  private async updatePin(pin: Pin, data: any): Promise<void> {
    await pin.setRelPins(data.rel_pin);
    await pin.setMedias(data.media);
  }
}

export const trailDAO = new TrailDAO();
