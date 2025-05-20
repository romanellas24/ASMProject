export interface UserInfo {
  address: string;
  mail: string;
  pwd: string;
}

export interface Local {
  id: number;
  name: string;
  openingTime: string;
  closingTime: string;
  address: string;
  openingDays: string;
  available: boolean;
}

export interface Menu {
  id: number;
  descritpion: string;
  type: string;
  price: number;
  localId: number;
}

export enum MenuType{
  MEAT = "Carne",
  FISH = "Pesce"
}

export enum TimeType{
  DINNER = "Cena",
  LUNCH = "Pranzo"
}