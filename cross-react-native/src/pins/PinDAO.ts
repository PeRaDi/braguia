import {Pin} from '@model/models.ts';
import {apiURL} from 'app.json';
import {CacheableDAO} from '@model/CacheableDAO.ts';

const csrftoken = 'tGJdUztKS1fwku3ZkjiUtKq0u281Q1nBKwnXCMnM7QSi04c7si8XdG50LpN7oSZ0';
const sessionid = 'xjn7ffpixtecp03v8j03j24tqak9gxan';

export class PinDAO extends CacheableDAO<Pin> {
  constructor() {
    super(Pin.table);
  }

  protected async fetchListFromRemote(): Promise<any[]> {
    const response = await fetch(`${apiURL}/pins`, {
                                                     method: 'GET',
                                                     headers: {
                                                       'Content-Type': 'application/json',
                                                       'X-CSRFToken': csrftoken,
                                                       'Cookie': `sessionid=${sessionid}`
                                                     },
                                                     credentials: 'include'
                                                   });
    console.log('-------------------------------------------URL pins:', `${apiURL}/pins`);
    const tmp = await response.json();
    console.log('Fetched pins from remote:', tmp);
    return (tmp) as any[];
  }

  protected async fetchOneFromRemote(id: string): Promise<any> {
    const response = await fetch(`${apiURL}/pin/${id}`, {
                                                          method: 'GET',
                                                          headers: {
                                                            'Content-Type': 'application/json',
                                                            'X-CSRFToken': csrftoken,
                                                            'Cookie': `sessionid=${sessionid}`
                                                          },
                                                          credentials: 'include'
                                                        });
    console.log('-------------------------------------------url pin:', `${apiURL}/pin/${id}`);
    return await response.json();
  }

  protected mapFromRemote(model: Pin, data: any): void {
    model.name = data.pin_name;
    model.description = data.pin_desc;
    console.log('-------------------------------------------Pin Name:', model.name);
    console.log('-------------------------------------------Pin Desc:', model.description);
  }
}

export const pinDAO = new PinDAO();
