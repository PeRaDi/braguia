export interface Social {
  social_name: string;
  social_url: string;
  social_share_link: string;
  social_app: string;
}

export interface Contact {
  contact_name: string;
  contact_phone: string;
  contact_url: string;
  contact_mail: string;
  contact_desc: string;
  contact_app: string;
}

export interface Partner {
  partner_name: string;
  partner_phone: string;
  partner_url: string;
  partner_mail: string;
  partner_desc: string;
  partner_app: string;
}

export interface AppInfo {
  app_name: string;
  socials: Social[];
  contacts: Contact[];
  partners: Partner[];
  app_desc: string;
  app_landing_page_text: string;
}
