package sample;

import com.opencsv.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tlist {

    private final ArrayList<fileInfo> trendList;
    private String impPath;
    private String exPath;
    private String trackingCountry;


    private Tlist() {
       this.trendList = new ArrayList<>();
    }
    private static final Tlist instance = new Tlist();

    public static Tlist getInstance() {
        return instance;
    }

    public ArrayList<fileInfo> getTrendList() {
        return trendList;
    }

    public String getTrackingCountry() {
        return trackingCountry;
    }
    public void  setImpPath(String impPath) {
        this.impPath = impPath;
    }

    public void setExPath(String exPath) {
        this.exPath = exPath;
    }

    public void setTrackingCountry(String trackingCountry) {
        this.trackingCountry = trackingCountry;
    }

    public static class fileInfo{
        private final ObservableList<Country> countries;
        private String date;
        private final String title;

        public fileInfo(ObservableList<Country> countries, String title) {
            this.countries = countries;
            this.title = title;
        }

        public ObservableList<Country> getCountries() {
            return countries;
        }

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class Country{
        private final String name;
        private final int interest;

        public String getName() {
            return name;
        }

        public int getInterest() {
            return interest;
        }

        public Country(String name, int interest) {
            this.name = name;
            this.interest = interest;

        }
    }
    public void csvReader(){
        if (impPath != null){
            try {
                String title ="";
                String date = "";
                ObservableList<Country> countryList = FXCollections.observableArrayList();

                FileReader filereader = new FileReader(impPath);

                CSVReader csvReader = new CSVReaderBuilder(filereader)
                        .withSkipLines(1)
                        .build();
                List<String[]> allData = csvReader.readAll();

                for (String[] row : allData) {
                    if (row.length>1 && !(row[0].contains("Country"))){
                        if ( !row[1].equals("") && !row[1].equals("<1") ){
                            countryList.add(new Country(row[0],Integer.parseInt(row[1])));
                        }else{
                            countryList.add(new Country(row[0],0));
                        }

                    }
                    else if (row[0].contains("Country")){
                        String[] part = row[1].split(":");
                        title = part[0];
                        date = part[1];


                    }
                }
                fileInfo newFile = new fileInfo(countryList,title);
                newFile.setDate(date);
                trendList.add(newFile);


            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void cvsWriter(){


        File file = new File(exPath+"\\"+trackingCountry+".csv");
        try {
            FileWriter outPutFile = new FileWriter(file,false);

            CSVWriter writer = new CSVWriter(outPutFile);

            String[] header = { "Trend", "Interest","Date"};
            writer.writeNext(header);
            for (Tlist.fileInfo f: trendList){
                for (Country country : f.getCountries()){
                    if (country.name.equals(trackingCountry)){

                        String[] data1 = { f.getTitle() , country.getInterest()+"" , f.getDate() };
                        writer.writeNext(data1);

                    }
                }
            }

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}


