import {Collection, Model} from '@nozbe/watermelondb';
import {children, field, relation, text, writer,} from '@nozbe/watermelondb/decorators';

export class Trail extends Model {
  static table = 'trails';

  static associations = {
    rel_trails: {type: 'has_many', foreignKey: 'trail_id'},
    edges: {type: 'has_many', foreignKey: 'trail_id'},
  };

  @text('name') name: string;
  @text('desc') description: string;
  @text('img') imageUrl: string;
  @field('duration') duration: number;
  @text('difficulty') difficulty: string;

  @children('rel_trails') rel_trails; // The resulting property will be a Query you can fetch, observe, or count.
  @children('edges') edges; // The resulting property will be a Query you can fetch, observe, or count.

  // get associatedPins() {
  // @lazy associatedPins = this.edges.extend(Q.experimentalJoinTables(['pins'])).pipe(
  //       Q.on('pins', Q.or(
  //           Q.where('id', Q.column('start_pin_id')),
  //           Q.where('id', Q.column('end_pin_id'))
  //       )),
  //       // Q.experimentalTake(999) // Assuming you want to limit the results, adjust as necessary
  //   );
  // }


  // @lazy
  // associatedPins = this.database.collections.get('pins').query(
  //     Q.experimentalJoinTables(['edges']),
  //     Q.on('edges', 'trail_id', this.id),
  //     Q.or(
  //         Q.where('id', Q.column('start_pin_id')),
  //         Q.where('id', Q.column('end_pin_id'))
  //     )
  // );

  static async associatedPins(trail: Trail): Promise<Pin[]> {
    // Fetch all edges associated with the trail
    const edgesData = await trail.edges.fetch();

    // For each edge, fetch the associated start and end pins
    const allPins: Pin[][] = await Promise.all(
        edgesData.map(async (edge: Edge) => {
          const startPin = await edge.startPin.fetch();
          const endPin = await edge.endPin.fetch();
          return [startPin as Pin, endPin as Pin];
        })
    );

    // Flatten the array of arrays into a one-dimensional array
    const flatPins = allPins.flat();
    return flatPins.filter((pin, index) => {
      return flatPins.findIndex(p => p.id === pin.id) === index;
    });
  }

  static async associatedMedias(trail: Trail): Promise<Media[]> {
    // Fetch all edges associated with the trail
    const edgesData = await trail.edges.fetch();

    // For each edge, fetch the associated start and end pins
    const allData: Media[][] = await Promise.all(
        edgesData.map(async (edge: Edge) => {
          const startPin = await edge.startPin.fetch();
          const endPin = await edge.endPin.fetch();
          const startMedias = await startPin.medias.fetch();
          const endMedias = await endPin.medias.fetch();
          return [...startMedias, ...endMedias];
        })
    );

    // Flatten the array of arrays into a one-dimensional array
    const flatted = allData.flat();
    return flatted.filter((pin, index) => {
      return flatted.findIndex(p => p.id === pin.id) === index;
    });
  }

  @writer
  async setEdges(data: any) {
    await this.edges.destroyAllPermanently();
    const edges: Edge[] = [];
    for (const d of data ?? []) {
      const edge = await this.addOrUpdateEdge(d);
      edges.push(edge);
    }
    return edges;
  }

  @writer
  async setRelTrails(data: any) {
    await this.rel_trails.destroyAllPermanently();
    for (const d of data ?? []) {
      await this.addOrUpdateRelTrail(d);
    }
  }

  async addOrUpdateEdge(d: any) {
    const coll = this.collections.get('edges') as Collection<Edge>;
    let edge: Edge | null = null;
    try {
      const saved = await coll.find(d.id.toString());
      edge = await saved.update((model: Edge) => this.mapEdge(model, d));
    } catch (error) {
      edge = await coll.create(model => {
        model._raw.id = d.id.toString();
        this.mapEdge(model, d);
      });
    }

    return edge;
  }

  async addOrUpdateRelTrail(d: any) {
    const coll = this.collections.get('rel_trails') as Collection<RelTrail>;
    try {
      const saved = await coll.find(d.id.toString());
      await saved.update((model: RelTrail) => this.mapRelTrail(model, d));
    } catch (error) {
      return await coll.create(model => {
        model._raw.id = d.id.toString();
        this.mapRelTrail(model, d);
      });
    }
  }

  private mapEdge(model: Edge, d: any): void {
    // model._setRaw('id', d.id.toString());
    model.trail.set(this);
    model.transport = d.edge_transport;
    model.duration = d.edge_duration;
    model.description = d.edge_desc;
  }

  private mapRelTrail(model: RelTrail, d: any): void {
    // model._setRaw('id', d.id.toString());
    model.trail.set(this);
    model.value = d.value;
    model.attrib = d.attrib;
  }
}

export class RelTrail extends Model {
  static table = 'rel_trails';

  static associations = {
    trails: {type: 'belongs_to', key: 'trail_id'},
  };

  @text('value') value: string;
  @text('attrib') attrib: string;

  @relation('trails', 'trail_id') trail; // returns a Relation object.
}

export class Edge extends Model {
  static table = 'edges';

  static associations = {
    trails: {type: 'belongs_to', key: 'trail_id'},
  };

  @text('transport') transport: string;
  @field('duration') duration: number;
  @text('desc') description: string;

  @relation('trails', 'trail_id') trail; // returns a Relation object.
  @relation('pins', 'start_pin_id') startPin; // returns a Relation object.
  @relation('pins', 'end_pin_id') endPin; // returns a Relation object.

  @writer
  async setStartEndPins(data: any) {
    const pins = this.collections.get('pins') as Collection<Pin>;
    const start = await this.savePin(pins, data.edge_start);
    const end = await this.savePin(pins, data.edge_end);
    await this.update(model => {
      model.startPin.set(start);
      model.endPin.set(end);
    });
    return {start, start_data: data.edge_start, end, end_data: data.edge_end};
  }

  private async savePin(pins: Collection<Pin>, data: any): Promise<Pin> {
    try {
      const pin = await pins.find(data.id.toString());
      return await pin.update(model => this.mapPin(model, data));
    } catch (error) {
      return await pins.create(model => {
        model._raw.id = data.id.toString();
        this.mapPin(model, data);
      });
    }
  }

  private mapPin(model: Pin, data: any): void {
    model.name = data.pin_name;
    model.description = data.pin_desc;
    model.latitude = data.pin_lat;
    model.longitude = data.pin_lng;
    model.altitude = data.pin_alt;
  }
}

export class Pin extends Model {
  static table = 'pins';

  static associations = {
    rel_pins: {type: 'has_many', foreignKey: 'pin_id'},
    medias: {type: 'has_many', foreignKey: 'pin_id'},
  };

  @text('name') name: string;
  @text('desc') description: string;
  @field('lat') latitude: number;
  @field('lng') longitude: number;
  @field('alt') altitude: number;

  @children('rel_pins') rel_pins; // The resulting property will be a Query you can fetch, observe, or count.
  @children('medias') medias; // The resulting property will be a Query you can fetch, observe, or count.

  static async images(pin: Pin): Promise<Media[]> {
    const medias = await pin.medias.fetch() as Media[];
    return medias.filter(m => m.isImage);
  }

  static async firstImage(pin: Pin): Promise<Media | null> {
    const images = await Pin.images(pin);
    return images.length == 0 ? null : images[0];
  }

  @writer
  async setRelPins(data: any) {
    await this.rel_pins.destroyAllPermanently();
    for (const d of data ?? []) {
      await this.addOrUpdateRelPin(d);
    }
  }

  @writer
  async setMedias(data: any) {
    await this.medias.destroyAllPermanently();
    for (const d of data ?? []) {
      await this.addOrUpdateMedia(d);
    }
  }

  async addOrUpdateRelPin(d: any) {
    const coll = this.collections.get('rel_pins') as Collection<RelPin>;
    try {
      const saved = await coll.find(d.id.toString());
      await saved.update(model => this.mapRelPin(model, d));
    } catch (error) {
      return await coll.create(model => {
        model._raw.id = d.id.toString();
        this.mapRelPin(model, d);
      });
    }
  }

  async addOrUpdateMedia(d: any) {
    const coll = this.collections.get('medias') as Collection<Media>;
    try {
      const saved = await coll.find(d.id.toString());
      return await saved.update(model => this.mapMedia(model, d));
    } catch (error) {
      return await coll.create(model => {
        model._raw.id = d.id.toString();
        this.mapMedia(model, d);
      });
    }
  }

  private mapRelPin(model: RelPin, d: any): void {
    // model._setRaw('id', d.id.toString());
    model.pin.set(this);
    model.value = d.value;
    model.attrib = d.attrib;
  }

  private mapMedia(model: Media, d: any): void {
    // model._setRaw('id', d.id.toString());
    model.pin.set(this);
    model.fileUrl = d.media_file;
    model.type = d.media_type;
  }
}

export class RelPin extends Model {
  static table = 'rel_pins';

  static associations = {
    pins: {type: 'belongs_to', key: 'pin_id'},
  };

  @text('value') value: string;
  @text('attrib') attrib: string;

  @relation('pins', 'pin_id') pin; // returns a Relation object.
}

export class Media extends Model {
  static table = 'medias';

  static associations = {
    pins: {type: 'belongs_to', key: 'pin_id'},
  };

  @text('file') fileUrl: string;
  @text('type') type: string;

  @relation('pins', 'pin_id') pin; // returns a Relation object.

  get isImage() {
    return this.type == 'I';
  }

  get isVideo() {
    return this.type == 'V';
  }

  get isRecord() {
    return this.type == 'R';
  }
}

export const models = [Trail, RelTrail, Edge, Pin, RelPin, Media];
