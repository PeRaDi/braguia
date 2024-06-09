import {Model} from '@nozbe/watermelondb';
import {children, field, relation, text} from '@nozbe/watermelondb/decorators';

export class Trail extends Model {
  static table = 'trails';

  static associations = {
    relTrails: {type: 'has_many', foreignKey: 'trail_id'},
    edges: {type: 'has_many', foreignKey: 'trail_id'},
  };

  @text('name') name: string;
  @text('desc') description: string;
  @text('img') imageUrl: string;
  @field('duration') duration: number;
  @text('difficulty') difficulty: string;

  @children('rel_trails') relTrails; // The resulting property will be a Query you can fetch, observe, or count.
  @children('edges') edges; // The resulting property will be a Query you can fetch, observe, or count.
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
}

export class Pin extends Model {
  static table = 'pins';

  static associations = {
    relPins: {type: 'has_many', foreignKey: 'pin_id'},
    medias: {type: 'has_many', foreignKey: 'pin_id'},
  };

  @text('name') name: string;
  @text('desc') description: string;
  @field('lat') latitude: number;
  @field('lng') longitude: number;
  @field('alt') altitude: number;

  @children('rel_pins') relPins; // The resulting property will be a Query you can fetch, observe, or count.
  @children('medias') medias; // The resulting property will be a Query you can fetch, observe, or count.
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
    return (this.type == 'I');
  }

  get isVideo() {
    return (this.type == 'V');
  }

  get isRecord() {
    return (this.type == 'R');
  }
}

export const models = [Trail, RelTrail, Edge, Pin, RelPin, Media];
