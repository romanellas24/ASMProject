export interface UserInfo {
  id:number;
  address: string;
  mail: string;
  pwd: string;
}

export interface GeneralResponse{
  message:string
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

export interface OrderInfo {
  userId: number;
  id?: number;
  deliveryTime: string;
  purchaseTime: string;
  transactionId: string;
  deliveryCompanyId:number;
  price: number;
  menuId: number;
  quantity: number;
  localId: number;
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

export interface Dish {
  id: number
  description: string
  name: string
  price: number
  menuId: number
}
