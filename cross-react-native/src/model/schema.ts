import {appSchema, tableSchema} from '@nozbe/watermelondb';

const trailSchema = tableSchema({
  name: 'trails',
  columns: [
    {name: 'id_api', type: 'number'},
    {name: 'img', type: 'string'},
    {name: 'name', type: 'string'},
    {name: 'desc', type: 'string'},
    {name: 'duration', type: 'number'},
    {name: 'difficulty', type: 'string'},
    {name: 'visited', type: 'boolean'},
  ],
});

const relTrailSchema = tableSchema({
  name: 'rel_trails',
  columns: [
    {name: 'id_api', type: 'number'},
    {name: 'value', type: 'string'},
    {name: 'attrib', type: 'string'},
    {name: 'trail_id', type: 'string', isIndexed: true},
  ],
});

const edgeSchema = tableSchema({
  name: 'edges',
  columns: [
    {name: 'id_api', type: 'number'},
    {name: 'transport', type: 'string'},
    {name: 'duration', type: 'number'},
    {name: 'desc', type: 'string'},
    {name: 'trail_id', type: 'string', isIndexed: true},
    {name: 'start_pin_id', type: 'string', isIndexed: true},
    {name: 'end_pin_id', type: 'string', isIndexed: true},
  ],
});

const pinSchema = tableSchema({
  name: 'pins',
  columns: [
    {name: 'id_api', type: 'number'},
    {name: 'name', type: 'string'},
    {name: 'desc', type: 'string'},
    {name: 'lat', type: 'number'},
    {name: 'lng', type: 'number'},
    {name: 'alt', type: 'number'},
    {name: 'visited', type: 'boolean'},
  ],
});

const mediaSchema = tableSchema({
  name: 'medias',
  columns: [
    {name: 'id_api', type: 'number'},
    {name: 'file', type: 'string'},
    {name: 'type', type: 'string'},
    {name: 'pin_id', type: 'string', isIndexed: true},
  ],
});

const relPinSchema = tableSchema({
  name: 'rel_pins',
  columns: [
    {name: 'id_api', type: 'number'},
    {name: 'value', type: 'string'},
    {name: 'attrib', type: 'string'},
    {name: 'pin_id', type: 'string', isIndexed: true},
  ],
});

export default appSchema({
  version: 2,
  tables: [
    trailSchema,
    relTrailSchema,
    edgeSchema,
    pinSchema,
    mediaSchema,
    relPinSchema,
  ],
});
