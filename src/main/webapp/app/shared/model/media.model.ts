import { IPost } from 'app/shared/model/post.model';
import { TypeMedia } from 'app/shared/model/enumerations/type-media.model';

export interface IMedia {
  id?: number;
  url?: string;
  typeMedia?: keyof typeof TypeMedia;
  post?: IPost | null;
}

export const defaultValue: Readonly<IMedia> = {};
