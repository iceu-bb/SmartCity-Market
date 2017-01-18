package EmployeeGui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import EmployeeContracts.IManager;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ManageCatalogProductTab implements Initializable {

	@FXML
	private RadioButton addCatalogProductRadioButton;

	@FXML
	private RadioButton removeCatalogProductRadioButton;

	@FXML
	private TextField barcodeTextField;

	@FXML
	private GridPane addCatalogProductParamPane;

	@FXML
	private TextField productNameTextField;

	@FXML
	private TextField productDescriptionTextField;

	@FXML
	private TextField productManufacturerTextField;

	@FXML
	private TextField productPriceTextField;

	@FXML
	private TextField productIngredientsTextField;

	@FXML
	private TextField productLocationTextField;

	@FXML
	private Button runTheOperationButton;

	IManager manager;

	RadioButtonEnabler radioButtonContainerManageCatalogProduct = new RadioButtonEnabler();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		productDescriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		;
		productManufacturerTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		productIngredientsTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productLocationTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});

		radioButtonContainerManageCatalogProduct.addRadioButtons(
				(Arrays.asList((new RadioButton[] { addCatalogProductRadioButton, removeCatalogProductRadioButton }))));
		
		enableRunOperation();

	}
	
	private void enableRunOperation() {
		runTheOperationButton.setDisable(barcodeTextField.getText().isEmpty() || productNameTextField.getText().isEmpty() || productDescriptionTextField.getText().isEmpty());
	}

	@FXML
	void addCatalogProductRadioButtonPressed(ActionEvent event) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(addCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(true);

	}

	@FXML
	void removeCatalogProductRadioButtonPressed(ActionEvent event) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(removeCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(false);
	}

	@FXML
	void runTheOperationButtonPressed(ActionEvent event) {

		try {
			if (addCatalogProductRadioButton.isSelected()) {

//				CatalogProduct catalogProduct = new CatalogProduct(Long.parseLong(barcodeTextField.getText()),
//						productNameTextField.getText(), null, null, productDescriptionTextField.getText(),
//						Double.parseDouble(productPriceTextField.getText()), null, null);

				// TODO Change this
				manager.addProductToCatalog(null);

			} else if (removeCatalogProductRadioButton.isSelected()) {

				// TODO Change this
				manager.removeProductFromCatalog(null);
			}

		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
	}

}
