package ule.edi.travel;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ule.edi.model.*;

public class TravelArrayImpl implements Travel {
	
	private static final Double DEFAULT_PRICE = 100.0;
	private static final Byte DEFAULT_DISCOUNT = 25;
	private static final Byte CHILDREN_EXMAX_AGE = 18;
	private Date travelDate;
	private int nSeats;
	
	private Double price;    // precio de entradas 
	private Byte discountAdvanceSale;   // descuento en venta anticipada (0..100)
   	
	private Seat[] seats;


	
   public TravelArrayImpl(Date date, int nSeats){
	   this.travelDate=date;
	   this.nSeats=nSeats;
	   this.price=DEFAULT_PRICE;
	   this.discountAdvanceSale=DEFAULT_DISCOUNT;
	   seats=new Seat[nSeats];
   }
   
   
   public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount){
	   this.travelDate=date;
	   this.nSeats=nSeats;
	   this.price=price;
	   this.discountAdvanceSale=discount;
	   seats=new Seat[nSeats];
   }






@Override
public Byte getDiscountAdvanceSale() {
	return discountAdvanceSale;
}


@Override
public int getNumberOfSoldSeats() {
	int nSoldSeats=0;
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null) {
			nSoldSeats++;           
		}
	}
	return nSoldSeats;
}


@Override
public int getNumberOfNormalSaleSeats() {
	int nNormalSale=0;
	for (Seat seat : seats) {
        if (seat!=null && seat.getHolder()!=null && !seat.getAdvanceSale()) {
            nNormalSale++;
        }
    }
    return nNormalSale;
}


@Override
public int getNumberOfAdvanceSaleSeats() {
	int nAdvanceSale=0;
	for (Seat seat : seats) {
		if (seat!=null && seat.getHolder()!=null && seat.getAdvanceSale()) {
			nAdvanceSale++;
		}
	}
	return nAdvanceSale;
}


@Override
public int getNumberOfSeats() {
	return nSeats;
}


@Override
public int getNumberOfAvailableSeats() {
	int nAvailableSeats=0;
	for(int i=0;i<seats.length;i++) {
		if(seats[i]==null) {
			nAvailableSeats++;           
		}
	}
	return nAvailableSeats;
}

@Override
public Seat getSeat(int pos) {
	return seats[pos];
}


@Override
public Person refundSeat(int pos) {
	if (pos<1 || pos>nSeats) {
		return null;
	}
	else{
		if(seats[pos-1]!=null) {
			Person p=seats[pos-1].getHolder();
			seats[pos-1]=null;
			return p;
		}
	}
	return null;
}


private boolean isChildren(int age) {
	if(age<CHILDREN_EXMAX_AGE){
		return true;
	}
	return false;
}


private boolean isAdult(int age) {
	if(age>=CHILDREN_EXMAX_AGE){
		return true;
	}
	return false;
}


@Override
public List<Integer> getAvailableSeatsList() {
	List<Integer> lista=new ArrayList<Integer>(nSeats);
	for(int i=0;i<seats.length;i++) {
		if(seats[i]==null) {
			lista.add(i+1);
		}
	}
	return lista;
}


@Override
public List<Integer> getAdvanceSaleSeatsList() {
	List<Integer> lista=new ArrayList<Integer>(nSeats);
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null) {
			if(seats[i].getAdvanceSale()) {
				lista.add(i+1);
			}
		}
	}
	
	return lista;
}


@Override
public int getMaxNumberConsecutiveSeats() {
	int maxNumConsecutiveSeats=0;
	int temp=maxNumConsecutiveSeats;

	for(int i=0;i<seats.length-1;i++) {
		if(seats[i]==null) {
			if (seats[i+1]==null) {
				maxNumConsecutiveSeats++;
			}
			else{
				if (temp<maxNumConsecutiveSeats) {
					temp=maxNumConsecutiveSeats;
					
				}
				maxNumConsecutiveSeats=1;
			}
		}
	}
	maxNumConsecutiveSeats=temp;
	return maxNumConsecutiveSeats;
}


@Override
public boolean isAdvanceSale(Person p) {
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null) {
			if(seats[i].getHolder().getNif().equals(p.getNif())) {
				return seats[i].getAdvanceSale();
			}
		}
	}
	return false;
}


@Override
public Date getTravelDate() {
	return travelDate;
}


@Override
public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {
    if (pos >= 1 && pos <= nSeats && seats[pos - 1] == null) {
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder().getNif().equals(nif)) {
                return false;
            }
        }
        seats[pos - 1] = new Seat(isAdvanceSale, new Person(nif, name, edad));
        return true;
    }
    return false;
}


@Override
public int getNumberOfChildren() {
	int nChildren=0;
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null) {
			if(isChildren(seats[i].getHolder().getAge())) {
				nChildren++;           
			}
		}
	}
	return nChildren;
}


@Override
public int getNumberOfAdults() {
	int nAdults=0;
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null) {
			if(isAdult(seats[i].getHolder().getAge())) {
				nAdults++;           
			}
		}
	}
	return nAdults;
}


@Override
public Double getCollectionTravel() {
	double collection=0;
	
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null&&seats[i].getHolder()!=null) {
				collection+=getSeatPrice(seats[i]);			
		}
	}
	return collection;
}


@Override
public int getPosPerson(String nif) {
	for(int i=0;i<seats.length;i++) {
		if(seats[i]!=null) {
			if(seats[i].getHolder().getNif().equals(nif)) {
				return i+1;
			}
		}
	}
	return -1;	
}


@Override
public int sellSeatFrontPos(String nif, String name, int edad, boolean isAdvanceSale) {
	for(int i=0;i<seats.length;i++) {
		if(seats[i]==null) {
			seats[i]=new Seat(isAdvanceSale,new Person(nif,name,edad));
			return i+1;
		}
	}
	return -1;
}


@Override
public int sellSeatRearPos(String nif, String name, int edad, boolean isAdvanceSale) {
	for(int i=seats.length-1;i>=0;i--) {
		if(seats[i]==null) {
			seats[i]=new Seat(isAdvanceSale,new Person(nif,name,edad));
			return i+1;
		}
	}
	return -1;
}


@Override
public Double getSeatPrice(Seat seat) {
		if(seat!=null&&seat.getAdvanceSale()) {
			return this.price*(1 - this.discountAdvanceSale / 100.0);
		}
		else{
			return this.price;
		}
	
	}



@Override
public double getPrice() {
	return this.price;
}


}	