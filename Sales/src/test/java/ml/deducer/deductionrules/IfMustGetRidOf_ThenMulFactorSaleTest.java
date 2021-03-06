package ml.deducer.deductionrules;

import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import BasicCommonClasses.PlaceInMarket;
import api.types.sales.ProductSale;
import ml.common.property.AProperty;
import ml.common.property.deducedproperties.MustGetRidOfPackageProperty;
import ml.common.property.saleproperty.ProductSaleByMulFactorProperty;
import testmocks.DBMock;
import testmocks.StorePackageMock;

public class IfMustGetRidOf_ThenMulFactorSaleTest {

	@Test
	public void testDeduceProperties() {
		MustGetRidOfPackageProperty property =
				new MustGetRidOfPackageProperty(new StorePackageMock(DBMock.getProduct(1), 1, LocalDate.now(), PlaceInMarket.STORE),
						1);
		
		Set<AProperty> propertySet = new HashSet<>();
		propertySet.add(property);
		
		Set<? extends AProperty> resultProperty = 
				new IfMustGetRidOf_ThenMulFactorSale().deduceProperties(DBMock.getSalePref(), propertySet);
		
		assertEquals(1, resultProperty.size());
		assert resultProperty.contains(new ProductSaleByMulFactorProperty(property.getStorePackage(), 1, 1,
				DBMock.getSalePref().getMaxDiscount()));

		assertEquals(new ProductSale(DBMock.getProduct(1), 1, 0.5 * DBMock.getProduct(1).getPrice()),
				resultProperty.stream().filter(p -> p instanceof ProductSaleByMulFactorProperty)
						.map(p -> (ProductSaleByMulFactorProperty) p)
						.map((ProductSaleByMulFactorProperty p) -> p.getOffer()).collect(Collectors.toList()).get(0));
			
	}

}
