package sample;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    List<String> drewTrend = new ArrayList<>();


    @FXML
    private TableColumn<Tlist.Country,String> countryColumn;
    @FXML
    private TableColumn<Tlist.Country,Integer> interestColumn;
    @FXML
    private TableView<Tlist.Country> tableView;
    @FXML
    private LineChart<String,Integer> lineChart;
    @FXML
    private JFXComboBox<String> cb;
    @FXML
    private JFXComboBox<String> chartCountryName;
    @FXML
    private JFXComboBox<String> chartTrend;
    @FXML
    private JFXComboBox<String> trendList;
    @FXML
    private JFXToggleButton multipleTrend;
    @FXML
    private Label message;


    public void initialize(){
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interest"));
        
        chartCountryName.setOnAction(event -> {
            if (chartTrend.getValue()!=null){
                lineChart.getData().clear();
                XYChart.Series<String,Integer> series = new XYChart.Series<>();

                for (Tlist.fileInfo file : Tlist.getInstance().getTrendList() ){
                    if (file.getTitle().equals(chartTrend.getValue())){
                        for (Tlist.Country country : file.getCountries()){
                            if (country.getName().equals(chartCountryName.getValue())){
                                series.getData().add(new XYChart.Data<>(file.getDate(), country.getInterest()));
                            }
                        }
                    }

                }
                series.setName(chartCountryName.getValue());
                lineChart.getData().add(series);
            }

        });
        multipleTrend.setOnMouseClicked(event -> {
            if (!multipleTrend.isSelected()){
                drewTrend.clear();
                lineChart.getData().clear();
            }
        });
        chartTrend.setOnAction(event -> {
            if(multipleTrend.isSelected()){
                if (chartCountryName.getValue()!=null){
                    drewTrend.add(chartTrend.getValue());
                    lineChart.getData().clear();
                    for (String trend : drewTrend){
                        XYChart.Series<String,Integer> series = new XYChart.Series<>();
                        for (Tlist.fileInfo file : Tlist.getInstance().getTrendList() ){
                            if (file.getTitle().equals(trend)){
                                for (Tlist.Country country : file.getCountries()){
                                    if (country.getName().equals(chartCountryName.getValue())){
                                        series.getData().add(new XYChart.Data<>(file.getDate(), country.getInterest()));
                                    }
                                }

                            }
                        }
                        series.setName(trend);
                        lineChart.getData().add(series);
                    }
                }

            }else {
                if (chartCountryName.getValue()!=null){
                    lineChart.getData().clear();
                    XYChart.Series<String,Integer> series = new XYChart.Series<>();

                    for (Tlist.fileInfo file : Tlist.getInstance().getTrendList() ){
                        if (file.getTitle().equals(chartTrend.getValue())){
                            for (Tlist.Country country : file.getCountries()){
                                if (country.getName().equals(chartCountryName.getValue())){
                                    series.getData().add(new XYChart.Data<>(file.getDate(), country.getInterest()));
                                }
                            }
                        }

                    }
                    series.setName(chartCountryName.getValue());
                    lineChart.getData().add(series);
                }
            }

        });

        trendList.setOnAction(event -> {
            String title = trendList.getValue().split(" {2}")[0];
            String date = trendList.getValue().split(" {2}")[1];
            for (Tlist.fileInfo file : Tlist.getInstance().getTrendList()){
                if(file.getTitle().equals(title) && file.getDate().equals(date)){
                    tableView.getItems().setAll(file.getCountries());

                }

            }
        });


    }
    public void loadBtnClicked(){
        FileChooser fc = new FileChooser();
        List<File> selectedFile = fc.showOpenMultipleDialog(null);

        if (selectedFile!=null){

            for (File file:selectedFile){
                Tlist.getInstance().setImpPath(file.getPath());
                Tlist.getInstance().csvReader();
            }

            ObservableList<String> list = FXCollections.observableArrayList();
            for (Tlist.fileInfo file : Tlist.getInstance().getTrendList()){

                list.add(file.getTitle()+"  "+file.getDate());

            }
            trendList.getItems().setAll(list.sorted());

            ObservableList<String> list1 = FXCollections.observableArrayList();
            for (Tlist.Country country:Tlist.getInstance().getTrendList().get(0).getCountries()){
                list1.add(country.getName());
            }
            chartCountryName.getItems().setAll(list1.sorted());

            ObservableList<String> list2 = FXCollections.observableArrayList();
            for (Tlist.fileInfo file : Tlist.getInstance().getTrendList()){
                if (!list2.contains(file.getTitle())){
                    list2.add(file.getTitle());
                }
            }
            chartTrend.getItems().setAll(list2.sorted());


            ObservableList<String> list3 = FXCollections.observableArrayList();
            for (Tlist.Country country:Tlist.getInstance().getTrendList().get(0).getCountries()){
                list3.add(country.getName());
            }
            cb.getItems().setAll(list3.sorted());
        }
    }

    public void onExportClicked(){
        String trackingCountry = cb.getValue();
        Tlist.getInstance().setTrackingCountry(trackingCountry);
        if (Tlist.getInstance().getTrackingCountry()!=null){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);

            if (selectedDirectory!=null){
                Tlist.getInstance().setExPath(selectedDirectory.getAbsolutePath());
                Tlist.getInstance().cvsWriter();
                message.setText("  فایل در :  "+selectedDirectory.getAbsolutePath()+"قرار گرفت ");
            }
        }

    }

}
