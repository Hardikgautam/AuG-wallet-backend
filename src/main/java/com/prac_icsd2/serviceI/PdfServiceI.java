package com.prac_icsd2.serviceI;

import java.io.ByteArrayInputStream;

public interface PdfServiceI {

	ByteArrayInputStream createPdf();
	
	ByteArrayInputStream generateCustomerPdf(Integer customerId);
}
