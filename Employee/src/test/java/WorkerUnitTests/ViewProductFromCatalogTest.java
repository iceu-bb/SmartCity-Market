package WorkerUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-06-02 
 */
@RunWith(MockitoJUnitRunner.class)
public class ViewProductFromCatalogTest {

	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");

		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void ViewProductFromCatalogSuccessfulTest() {
		CatalogProduct testCatalogProduct = null, catalogProduct = new CatalogProduct(1234567890, "name", null,
				new Manufacturer(1, "Manufacturer"), "description", 22.0, null, null);
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
				Serialization.serialize(catalogProduct));
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize()))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			testCatalogProduct = worker.viewProductFromCatalog(1234567890);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		}
		
		assertEquals(testCatalogProduct.getBarcode(), 1234567890);
		assertEquals(testCatalogProduct.getManufacturer().getId(), 1);
		assertEquals(testCatalogProduct.getManufacturer().getName(), "Manufacturer");
		assertEquals(testCatalogProduct.getName(), "name");
	}

	@Test
	public void ViewProductFromCatalogproductNotFoundTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.viewProductFromCatalog(1234567890);
			
			fail();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (ProductNotExistInCatalog e) {
			/* Test Passed */
		}
	}
	
	@Test
	public void ViewProductFromCatalogproductSenderIsNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.viewProductFromCatalog(1234567890);
			
			fail();
		} catch (InvalidParameter  | CriticalError | ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		} catch (EmployeeNotConnected e) {
			/* Test Passed */
		}
	}
	
	@Test
	public void ViewProductFromCatalogproductIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			worker.viewProductFromCatalog(1234567890);
			
			fail();
		} catch (InvalidParameter  | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError e) {
			/* Test Passed */
		}
	}
}
