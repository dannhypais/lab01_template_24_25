package pt.pa.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.pa.model.Laptop;
import pt.pa.model.Review;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amfs
 */
public class LaptopsGui extends BorderPane {

    private static final String DATA_PATH = "src/main/resources/laptop_reviews.json";

    private static final String BANNER_PATH = "src/main/resources/header_banner.png";

    List<Laptop> laptops;

    ListView<Laptop> listViewLaptops;

    private VBox mainContent;


    public LaptopsGui() throws Exception {
        try {
            this.laptops = loadData();
            initializeComponents();
        } catch (FileNotFoundException e) {
            throw new Exception("Unable to initialize Laptops GUI");
        }
    }

    public void initializeComponents() throws FileNotFoundException {
     //TODO
        ImageView imageView = loadThumbnailImage();

        ListView<Laptop> listView = new ListView<Laptop>();
        ObservableList<Laptop> laptopList = FXCollections.observableArrayList(laptops);
        listView.setItems(laptopList);
        listView.getSelectionModel().select(0);

        Label laptopInfoTitle = new Label("Laptop Information");
        laptopInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        VBox laptopInfoVBox = new VBox();
        setTop(imageView);
        setLeft(listView);
        setCenter(laptopInfoVBox);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        laptopInfoVBox.getChildren().clear();
                        Label laptopInfo = new Label(newValue.toString());
                        Separator hr = new Separator();
                        Label laptopReviewTitle = new Label("Review");
                        laptopReviewTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                        laptopInfoVBox.getChildren().addAll(laptopInfoTitle, laptopInfo, hr, laptopReviewTitle);
                        for (Review review : newValue.getReviews()) {
                            Label laptopReview = new Label(review.toString());
                            laptopInfoVBox.getChildren().add(laptopReview);
                        }
                    }
                });
                Laptop selectedLaptop = listView.getSelectionModel().getSelectedItem();
                if(selectedLaptop != null) {
                    Label laptopInfo = new Label(selectedLaptop.toString());
                    Separator hr = new Separator();
                    Label laptopReviewTitle = new Label("Review");
                    laptopReviewTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    laptopInfoVBox.getChildren().addAll(laptopInfoTitle, laptopInfo, hr, laptopReviewTitle);
                    for(Review review : selectedLaptop.getReviews()){
                        Label laptopReview = new Label(review.toString());
                        laptopInfoVBox.getChildren().add(laptopReview);
                }
            }
        }

    /**
     * Load the data  contain on json file specified on DATA_PATH.
     * @return list of Lapstop contained on the file
     * @throws FileNotFoundException in case of the file not exists
     */
    private List<Laptop> loadData() throws FileNotFoundException {

        Gson gson = new Gson();

        Type arrayListType = new TypeToken<ArrayList<Laptop>>() {
        }.getType();

        return gson.fromJson(new FileReader(DATA_PATH), arrayListType);
    }

    /**
     * create an Image View from the image file specified on BANNER_PATH
     * @return the Image View created from the file specified on BANNER_PATH
     * @throws FileNotFoundException in case the file not exists
     */
    ImageView loadThumbnailImage() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(BANNER_PATH);
        Image image = new Image(inputStream);
        return new ImageView(image);
    }

}