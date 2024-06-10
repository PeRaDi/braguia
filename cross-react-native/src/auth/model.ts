export interface User {
  user_type: string;
  last_login: string;
  is_superuser: boolean;
  username: string;
  first_name: string;
  last_name: string;
  email: string;
  is_staff: boolean;
  is_active: boolean;
  date_joined: string;
  groups: any[]; // You can specify the type of group elements if known
  user_permissions: number[];
}
