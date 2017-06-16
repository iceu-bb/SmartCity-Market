package ml.extractor.dataminers;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import api.contracts.IStorePackage;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireStorePackageProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductProperty;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.StorePackageMock;

/**
 * @author noam yefet
 * @author idan atias
 *
 * @since Jun 11, 2017
 */
public class StoreStatisticsMinerTest {

	private static List<GroceryListMock> history = new ArrayList<>();
	private static List<StorePackageMock> stock = new ArrayList<>();
	private static StoreData sd = new StoreData(history, stock, DBMock.getCatalog());

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// test50MostPopularProperty
		for (long i = 1; i <= DBMock.NUM_OF_PRODUCTS; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));

		for (long i = 1; i <= 50; i++)
			history.add(new GroceryListMock("bob").addProdcut(DBMock.getProduct(i)));

		// testAboutToExpireProperty
		for (long i = 1; i <= DBMock.NUM_OF_PRODUCTS; i++) {
			if (i <= DBMock.NUM_OF_PRODUCTS - AboutToExpireStorePackageProperty.numOfTop)
				stock.add(new StorePackageMock(i, LocalDate.MAX)); // far E.D
			else
				stock.add(new StorePackageMock(i, LocalDate.now())); // close E.D
		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test50MostPopularProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductProperty && ((MostPopularProductProperty) p).getAmount() == 2)
				.count();

		for (long i = 1; i <= 50; i++)
			assertTrue(result.contains(new MostPopularProductProperty(DBMock.getProduct(i), 2)));

		assertEquals(50, numOfRightAmount);

	}

	@Test
	public void testAboutToExpireProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireStorePackages = result.stream()
				.filter(p -> p instanceof AboutToExpireStorePackageProperty).count();

		assertEquals(AboutToExpireStorePackageProperty.numOfTop, totalAboutToExpireStorePackages);
		
		for (IStorePackage spm : stock) {
		    long currSpmBarcode = spm.getProduct().getBarcode();
			 if (currSpmBarcode <= DBMock.NUM_OF_PRODUCTS - AboutToExpireStorePackageProperty.numOfTop)
				 assertFalse(result.contains(new AboutToExpireStorePackageProperty(spm)));
			 else
				 assertTrue(result.contains(new AboutToExpireStorePackageProperty(spm)));
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testStoreStatisticsMiner() {
		new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1)));
	}

}