package main.models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table (name = "championships")
public class Championship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "name")
    public String name;

    @Column(name = "date_from")
    public Date dateFrom;

    @Column(name = "date_to")
    public Date dateTo;

    @Column ( name = "city" )
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "order_number")
    private int orderNumber;

    @ManyToMany
    @JoinTable( name = "champ_disciplines",
                joinColumns = @JoinColumn(name = "championship_id"),
                inverseJoinColumns = @JoinColumn(name = "discipline_id"))
    private List<Discipline> disciplines;

    @OneToMany ( mappedBy = "championship", fetch = FetchType.EAGER)
    private List<User> users = new LinkedList<>();

    public Championship(){

    }

    public Championship ( String name , Date dateFrom , Date dateTo , String city , String country , String fullAddress , int orderNumber) {
        this.name        = name;
        this.dateFrom    = dateFrom;
        this.dateTo      = dateTo;
        this.city        = city;
        this.country     = country;
        this.fullAddress = fullAddress;
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber ( ) {
        return orderNumber;
    }

    public void setOrderNumber ( int orderNumber ) {
        this.orderNumber = orderNumber;
    }

    public String getName ( ) {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public String getCity ( ) {
        return city;
    }

    public void setCity ( String city ) {
        this.city = city;
    }

    public String getCountry ( ) {
        return country;
    }

    public void setCountry ( String country ) {
        this.country = country;
    }

    public String getFullAddress ( ) {
        return fullAddress;
    }

    public void setFullAddress ( String fullAddress ) {
        this.fullAddress = fullAddress;
    }
    public List<Discipline> getDisciplines ( ) {
        return disciplines;
    }

    public void setDisciplines ( List<Discipline> disciplines ) {
        this.disciplines = disciplines;
    }

    public List<User> getUsers ( ) {
        return users;
    }

    public Date getDateFrom ( ) {
        return dateFrom;
    }

    public void setDateFrom ( Date dateFrom ) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo ( ) {
        return dateTo;
    }

    public void setDateTo ( Date dateTo ) {
        this.dateTo = dateTo;
    }

    public void setUsers ( List<User> users ) {
        this.users = users;
    }

    public void addUser(User user){
        users.add(user);
    }
    public void addDiscipline(Discipline discipline){
        disciplines.add(discipline);
    }
}
