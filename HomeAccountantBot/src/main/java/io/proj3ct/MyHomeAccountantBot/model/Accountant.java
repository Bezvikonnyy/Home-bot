package io.proj3ct.MyHomeAccountantBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "accountantDataTable")
public class Accountant {

    @Id
    private Long chatId;

    private int income;

    private int cost;

    private int foodAtHome;

    private int foodForWork;

    private int commonUseItems;

    private int internetEntertainment;

    private int credit;

    private int rent;

    public Long getChatId() { return chatId; }

    public void setChatId(Long chatId) { this.chatId = chatId;}

    public int getIncome() { return income; }

    public void setIncome(int income) { this.income = income; }

    public int getCost() { return cost; }

    public void setCost(int cost) { this.cost = cost; }

    public int getFoodAtHome() { return foodAtHome; }

    public void setFoodAtHome(int foodAtHome) { this.foodAtHome = foodAtHome; }

    public int getFoodForWork() { return foodForWork; }

    public void setFoodForWork(int foodForWork) { this.foodForWork = foodForWork; }

    public int getCommonUseItems() { return commonUseItems; }

    public void setCommonUseItems(int commonUseItems) { this.commonUseItems = commonUseItems; }

    public int getInternetEntertainment() { return internetEntertainment; }

    public void setInternetEntertainment(int internetEntertainment) {
        this.internetEntertainment = internetEntertainment;
    }

    public int getCredit() { return credit; }

    public void setCredit(int credit) { this.credit = credit;}

    public int getRent() { return rent; }

    public void setRent(int rent) { this.rent = rent; }

    @Override
    public String toString() {
        return "Accountant{" +
                "chatId=" + chatId +
                ", income=" + income +
                ", cost=" + cost +
                ", foodAtHome=" + foodAtHome +
                ", foodForWork=" + foodForWork +
                ", commonUseItems=" + commonUseItems +
                ", internetEntertainment=" + internetEntertainment +
                ", credit=" + credit +
                ", rent=" + rent +
                '}';
    }
}
