import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CitibankStatementParser{
    private static HashMap<String, String> months = new HashMap<>();
    private static HashMap<String, String> itemTypes = new HashMap<>();
    private static String prevMonth = null;

    private static void createMonthMap(){
        months.put("Jan.", "01");
        months.put("Feb.", "02");
        months.put("Mar.", "03");
        months.put("Apr.", "04");
        months.put("May.", "05");
        months.put("Jun.", "06");
        months.put("Jul.", "07");
        months.put("Aug.", "08");
        months.put("Sep.", "09");
        months.put("Oct.", "10");
        months.put("Nov.", "11");
        months.put("Dec.", "12");
    }

    private static void createItemMaps(){
        itemTypes.put("UPS", "Misc");
        itemTypes.put("Spotify", "Misc");
        itemTypes.put("AMC Movie", "Misc");
        itemTypes.put("M2M", "Food");
        itemTypes.put("Seamless", "Food");
        itemTypes.put("Thai Market", "Food");
        itemTypes.put("Eden Salon", "Misc");
        itemTypes.put("Szechuan Garden", "Food");
        itemTypes.put("MTA", "Transportation");
        itemTypes.put("Ivy League Stationers", "Supplies");
        itemTypes.put("Westside", "Food");
        itemTypes.put("Haagen Dazs", "Food");
        itemTypes.put("Vending Machine", "Food");
        itemTypes.put("Amirs", "Food");
        itemTypes.put("Duane Reade", "Supplies");
        itemTypes.put("Vine", "Food");
        itemTypes.put("Sweetgreen", "Food");
        itemTypes.put("Chipotle", "Food");
        itemTypes.put("Soylent", "Food");
        itemTypes.put("Deluxe", "Food");
        itemTypes.put("Strokos", "Food");
        itemTypes.put("Dig Inn", "Food");
        itemTypes.put("iCloud", "Misc");
        itemTypes.put("Trader Joes", "Food");
        itemTypes.put("Boba Bear", "Food");
        itemTypes.put("Kotoya", "Food");
        itemTypes.put("Walgreens", "Supplies");
        itemTypes.put("Starbucks", "Food");
        itemTypes.put("Rockreation", "Misc");
        itemTypes.put("CVS", "Supplies");
        itemTypes.put("Jetblue", "Travel");
        itemTypes.put("Delta", "Travel");
        itemTypes.put("Cafe East", "Food");
        itemTypes.put("Community", "Food");
        itemTypes.put("Saiguette", "Food");
        itemTypes.put("Morton Williams", "Food");
        itemTypes.put("Taxi", "Transportation");
        itemTypes.put("Steep Rock", "Misc");
        itemTypes.put("RP", "Misc");
        itemTypes.put("Legend", "Food");
        itemTypes.put("Tea Magic", "Food");
        itemTypes.put("Amazon", "Misc");
        itemTypes.put("Shake Shack", "Food");
        itemTypes.put("Trader Joes", "Food");
        // Types: Transportation, Clothing, Food, Travel, Supplies, Misc
    }

    private static String parseItem(String unparsedItem){
        if(containsIgnoreCase(unparsedItem, "Trader Joe"))
            return "Trader Joes";
        if(containsIgnoreCase(unparsedItem, "SHAKE SHACK"))
            return "Shake Shack";
        if(containsIgnoreCase(unparsedItem, "PAYMENT THANK YOU"))
            return "Payment Thank You";
        if(containsIgnoreCase(unparsedItem, "Tea Magic"))
            return "Tea Magic";
        if(containsIgnoreCase(unparsedItem, "LEGEND UPPER WEST"))
            return "Legend";
        if(containsIgnoreCase(unparsedItem, "RiotGam*LoL"))
            return "RP";
        if(containsIgnoreCase(unparsedItem, "STEEP ROCK BOULDERING"))
            return "Steep Rock";
        if(containsIgnoreCase(unparsedItem, "TAXI"))
            return "Taxi";
        if(containsIgnoreCase(unparsedItem, "MORTON WILLIAM"))
            return "Morton Williams";
        if(containsIgnoreCase(unparsedItem, "SAIGUETTE"))
            return "Saiguette";
        if(containsIgnoreCase(unparsedItem, "COMMUNITY FOOD AND JUI"))
            return "Community";
        if(containsIgnoreCase(unparsedItem, "CAFE EAST"))
            return "Cafe East";
        if(containsIgnoreCase(unparsedItem, "JETBLUE"))
            return "Jetblue";
        if(containsIgnoreCase(unparsedItem, "DELTA"))
            return "Delta";
        if(containsIgnoreCase(unparsedItem, "CVS/PHARMACY"))
            return "CVS";
        if(containsIgnoreCase(unparsedItem, "ROCKREATION LOS ANGELES"))
            return "Rockreation";
        if(containsIgnoreCase(unparsedItem, "STARBUCKS"))
            return "Starbucks";
        if(containsIgnoreCase(unparsedItem, "WALGREENS"))
            return "Walgreens";
        if(containsIgnoreCase(unparsedItem, "KOTOYA RAMEN"))
            return "Kotoya";
        if(containsIgnoreCase(unparsedItem, "BOBA BEAR"))
            return "Boba Bear";
        if(containsIgnoreCase(unparsedItem, "TRADER JOES"))
            return "Trader Joes";
        if(containsIgnoreCase(unparsedItem, "ITUNES.COM"))
            return "iCloud";
        if(containsIgnoreCase(unparsedItem, "Dig Inn Seasonal Mar"))
            return "Dig Inn";
        if(containsIgnoreCase(unparsedItem, "STROKOS GOURMET DEL"))
            return "Strokos";
        if(containsIgnoreCase(unparsedItem, "DELUXE NEW YORK"))
            return "Deluxe";
        if(containsIgnoreCase(unparsedItem, "SOYLENT"))
            return "Soylent";
        if(containsIgnoreCase(unparsedItem, "CHIPOTLE"))
            return "Chipotle";
        if(containsIgnoreCase(unparsedItem, "SWEETGREEN"))
            return "Sweetgreen";
        if(containsIgnoreCase(unparsedItem, "VINE SUSHI"))
            return "Vine";
        if(containsIgnoreCase(unparsedItem, "DUANE READE"))
            return "Duane Reade";
        if(containsIgnoreCase(unparsedItem, "AMIRS NEW YORK"))
            return "Amirs";
        if(containsIgnoreCase(unparsedItem, "CMSVEND*CV"))
            return "Vending Machine";
        if(containsIgnoreCase(unparsedItem, "HAAGEN DAZS"))
            return "Haagen Dazs";
        if(containsIgnoreCase(unparsedItem, "WESTSIDE SUPERMARKET"))
            return "Westside";
        if(containsIgnoreCase(unparsedItem, "IVY LEAGUE STATIONERS"))
            return "Ivy League Stationers";
        if(containsIgnoreCase(unparsedItem, "MTA MVM"))
            return "MTA";
        if(containsIgnoreCase(unparsedItem, "SZECHUAN GARDEN"))
            return "Szechuan Garden";
        if(containsIgnoreCase(unparsedItem, "EDEN SALON N SPA"))
            return "Eden Salon";
        if(containsIgnoreCase(unparsedItem, "THAI MARKET INC"))
            return "Thai Market";
        if(containsIgnoreCase(unparsedItem, "SEAMLSS"))
            return "Seamless";
        if(containsIgnoreCase(unparsedItem, "M2M MART BROADWAY"))
            return "M2M";
        if(containsIgnoreCase(unparsedItem, "AMC"))
            return "AMC Movie";
        if(containsIgnoreCase(unparsedItem, "THE UPS STORE"))
            return "UPS";
        if(containsIgnoreCase(unparsedItem, "Spotify"))
            return "Spotify";
        if(containsIgnoreCase(unparsedItem, "AMAZON.COM") || containsIgnoreCase(unparsedItem, "AMAZON MKTPLACE"))
            return "Amazon";
        return unparsedItem;
    }

	public static boolean containsIgnoreCase(String s1, String s2) {
  		if(s2.equals(""))
  			return true;
  		if(s1 == null || s2 == null || s1.equals(""))
  		  	return false;

  		Pattern p = Pattern.compile(s2,Pattern.CASE_INSENSITIVE|Pattern.LITERAL);
  		Matcher m = p.matcher(s1);
  		return m.find();
	}

    private static String parseLine (String line){
        String[] elmts = line.split("\t");
        String unparsedItem = elmts[1];
        unparsedItem = unparsedItem.substring(0, unparsedItem.indexOf("Expand Details")-1);

        String date = elmts[0];
        String[] dateElmts = date.split(" ");
        String month = months.get(dateElmts[0]);
        date = month + "/"
            + dateElmts[1].substring(0, 2) + "/"
            + dateElmts[2];

        String item = parseItem(unparsedItem);
        if (item.equals("Payment Thank You")){
            return null;
        }
        String type = (itemTypes.containsKey(item)) ? itemTypes.get(item) : "Food";
        String amount = elmts[2].replaceAll("(?<=\\d),(?=\\d)|\\$", "");

        String expense;
        if (prevMonth == null){
            prevMonth = month;
            expense = date + "\t" + amount + "\t" + type + "\t" + item + "\t" + unparsedItem + "\n";
        } else if (!prevMonth.equals(month)){
            prevMonth = month;
            expense = "\n" + date + "\t" + amount + "\t" + type + "\t" + item + "\t" + unparsedItem + "\n";
        } else {
            expense = date + "\t" + amount + "\t" + type + "\t" + item + "\t" + unparsedItem + "\n";
        }

        return expense;
    }

    public static void main(String[] args){
        createMonthMap();
        createItemMaps();

        ArrayList<String> lines = new ArrayList<>();
        String filename = args[0];

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(s -> lines.add(0, s));
        } catch (IOException e){
            e.printStackTrace();
        }

        Charset charset = Charset.forName("UTF-8");
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime());
        try {
            // Path outputfile = Files.createFile(Paths.get("~/Downloads/citibank_expenses_parsed_" + timestamp + ".txt"));
            // Path path = Paths.get("~/Downloads/citibank_expenses_parsed_" + timestamp + ".txt");
            Path outputfile = Paths.get(filename);
            // Path outputfile = Files.createFile(path);
            try (BufferedWriter writer = Files.newBufferedWriter(outputfile, charset)) {
                for (String line: lines){
                    String expense = parseLine(line);
                    if (expense != null && !expense.isEmpty()) {
                        writer.write(expense, 0, expense.length());
                    }
                }
            } catch (IOException e2) {
               e2.printStackTrace();
            }
        // } catch (IOException e1) {
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
