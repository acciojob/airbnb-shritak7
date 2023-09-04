package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository

public class HotelManagementRepository {


    HashMap<String,Hotel>hotelDb=new HashMap<>();
    HashMap<Integer,User>userDb=new HashMap<>();
    HashMap<String,Booking>bookingDb=new HashMap<>();





    public String addHotel(Hotel hotel) {
        String hotelname=hotel.getHotelName();
        if(hotelname==null){
            return "Failure";
        }
        if(hotelDb.containsKey(hotelname)){
            return "Failure";
        }
        else{
            hotelDb.put(hotelname,hotel);
            return "Hotel added Successfully";
        }
    }

    public Integer addUser(User user) {
        //String username=user.getName();
        int aadhar=user.getaadharCardNo();

        userDb.put(aadhar,user);
        return aadhar;
    }

    public String getHotelWithMostFacilities() {
        int maxfacility=0;
        String str="";

        for(String key:hotelDb.keySet()){
            List<Facility> ans=hotelDb.get(key).getFacilities();
            int size=ans.size();
            if(size>maxfacility  ){
                maxfacility=size;
                str=key;
            }
            else if(size==maxfacility){
                if(key.compareTo(str)<0){
                    str=key;
                }
            }
        }
        if(maxfacility==0){
            return "";
        }
        else{
            return str;
        }
    }

    public int bookARoom(Booking booking) {

        UUID uuid=UUID.randomUUID();
        String ans=uuid.toString();
        booking.setBookingId(ans);

        String name=booking.getHotelName();
        Hotel hotel=hotelDb.get(name);

        int noofrooms=hotel.getAvailableRooms();
        int bookingrooms=booking.getNoOfRooms();
        int priceperday=hotel.getPricePerNight();

        if(noofrooms<bookingrooms){
            return -1;
        }
        int totalamount=priceperday*bookingrooms;
        booking.setAmountToBePaid(totalamount);

        hotel.setAvailableRooms(noofrooms-bookingrooms);
        bookingDb.put(ans,booking);
        hotelDb.put(name,hotel);

        return totalamount;

    }

    public int getBookings(Integer aadharCard) {

        int count=0;

        for(String ans:bookingDb.keySet()){
            if(bookingDb.get(ans).getBookingAadharCard()==aadharCard){
                count++;
            }
        }

        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel=hotelDb.get(hotelName);
        List<Facility>oldfacilities=hotel.getFacilities();

        for(Facility facility:newFacilities){
            if(oldfacilities.contains(facility)){
                continue;
            }
            else{
                oldfacilities.add(facility);
            }
        }
        hotel.setFacilities(oldfacilities);
        hotelDb.put(hotelName,hotel);

        return hotel;


    }
}
