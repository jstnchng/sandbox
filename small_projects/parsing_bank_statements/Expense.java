package citiparser;

public class Expense{
    private String date;
    private String amount;
    private String type;
    private String item;
    private String unparsedItem;

    public Expense(String date, String amount, String type, String item, String unparsedItem){
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.item = item;
        this.unparsedItem = unparsedItem;
    }

    public String getDate(){
        return date;
    }

    public String getAmount(){
        return amount;
    }

    public String getType(){
        return type;
    }

    public String getItem(){
        return item;
    }

    public String getUnparsedItem(){
        return unparsedItem;
    }
}
