import {Pin} from '@model/models.ts';
import {CacheableDAO} from '@model/CacheableDAO.ts';
import axiosInstance from '@src/network/axios.config.ts';

export class PinDAO extends CacheableDAO<Pin> {
  constructor() {
    super(Pin.table);
  }

  protected async fetchListFromRemote(): Promise<any[]> {
    const response = await axiosInstance.get('/pins');
    return response.data as any[];
  }

  protected async fetchOneFromRemote(id: string): Promise<any> {
    const response = await axiosInstance.get(`/pin/${id}`);
    return response.data;
  }

  protected mapFromRemote(model: Pin, data: any): void {
    model.name = data.pin_name;
    model.description = data.pin_desc;
    model.latitude = data.pin_lat;
    model.longitude = data.pin_lng;
    model.altitude = data.pin_alt;
    model.media = data.media
  }

  protected async afterModelSet(model: Pin, data: any): Promise<void> {
    await model.setRelPins(data.rel_pin);
    await model.setMedias(data.media);
  }
}

export const pinDAO = new PinDAO();
