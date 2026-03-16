package com.prac_icsd2.ServiceImpl;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.openpdf.text.Chunk;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prac_icsd2.model.Account;
import com.prac_icsd2.model.Address;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.serviceI.PdfServiceI;

import jakarta.transaction.Transactional;

@Service
public class PdfService implements PdfServiceI {

	private Logger logger = LoggerFactory.getLogger(PdfService.class);

	@Autowired
	private CustomerRepo customerRepository;

	@Override
	public ByteArrayInputStream createPdf() {
		logger.info("creation of pdf started.....");
		String title = "Welcome Here is Your Pdf";
		String content = "Here is our content";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
		Paragraph titlePara = new Paragraph(title, titleFont);
		titlePara.setAlignment(Element.ALIGN_CENTER);
		document.add(titlePara);
		Font paraFont = FontFactory.getFont(FontFactory.HELVETICA, 18);
		Paragraph paragraph = new Paragraph(content, paraFont);
		paragraph.add(new Chunk("Wow this text is Added after creating PAragerph"));
		document.add(paragraph);
		document.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	@Transactional
	public ByteArrayInputStream generateCustomerPdf(Integer customerId) {
		logger.info("Generating customer PDF for customerId={}", customerId);

		Customer customer = customerRepository.findByIdWithAllDetails(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();

		Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.DARK_GRAY);
		Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(33, 97, 140));
		Font labelFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
		Font valueFont   = FontFactory.getFont(FontFactory.HELVETICA,      11, Color.BLACK);
		Font smallFont   = FontFactory.getFont(FontFactory.HELVETICA,       9, Color.GRAY);

		Paragraph title = new Paragraph("Customer Information Report", headingFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(4f);
		document.add(title);

		Paragraph subTitle = new Paragraph(
				"Generated for: " + customer.getFirstName() + " " + customer.getLastName(), smallFont);
		subTitle.setAlignment(Element.ALIGN_CENTER);
		subTitle.setSpacingAfter(16f);
		document.add(subTitle);

		addHorizontalLine(document);

		addSectionHeading(document, "1. Personal Details", sectionFont);
		PdfPTable personalTable = createTwoColTable();
		addRow(personalTable, "Customer ID",      String.valueOf(customer.getCustomerId()),                  labelFont, valueFont);
		addRow(personalTable, "First Name",        customer.getFirstName(),                                  labelFont, valueFont);
		addRow(personalTable, "Last Name",         customer.getLastName(),                                   labelFont, valueFont);
		addRow(personalTable, "Email",             customer.getEmailId(),                                    labelFont, valueFont);
		addRow(personalTable, "Contact No",        customer.getContactNo(),                                  labelFont, valueFont);
		addRow(personalTable, "Gender",            customer.getGender() != null
				? customer.getGender().name() : "N/A",                                                       labelFont, valueFont);
		addRow(personalTable, "Registration Date", customer.getRegisterationDate() != null
				? customer.getRegisterationDate().toString() : "N/A",                                        labelFont, valueFont);
		document.add(personalTable);

		addSectionHeading(document, "2. Address", sectionFont);
		Address addr = customer.getAddress();
		if (addr != null) {
			PdfPTable addrTable = createTwoColTable();
			addRow(addrTable, "Address Line 1", addr.getAddressline1(), labelFont, valueFont);
			addRow(addrTable, "Address Line 2", addr.getAddressline2(), labelFont, valueFont);
			addRow(addrTable, "City",           addr.getCity(),         labelFont, valueFont);
			addRow(addrTable, "State",          addr.getState(),        labelFont, valueFont);
			addRow(addrTable, "Pincode",        addr.getPincode(),      labelFont, valueFont);
			document.add(addrTable);
		} else {
			document.add(new Paragraph("No address information available.", valueFont));
		}

		addSectionHeading(document, "3. Accounts", sectionFont);
		if (customer.getAccounts() != null && !customer.getAccounts().isEmpty()) {
			PdfPTable accTable = new PdfPTable(new float[]{1f, 2f, 2f, 2f});
			accTable.setWidthPercentage(100);
			accTable.setSpacingBefore(6f);
			accTable.setSpacingAfter(12f);
			addTableHeader(accTable, new String[]{"#", "Account No", "Account Type", "Opening Balance"});
			int idx = 1;
			for (Account acc : customer.getAccounts()) {
				addRow(accTable,
						String.valueOf(idx++),
						String.valueOf(acc.getAccountnumber()),
						acc.getAccountType() != null ? acc.getAccountType().name() : "N/A",
						acc.getOpeningBalance() != null ? "Rs. " + acc.getOpeningBalance() : "N/A",
						labelFont, valueFont);
			}
			document.add(accTable);
		} else {
			document.add(new Paragraph("No accounts linked to this customer.", valueFont));
		}

		addSectionHeading(document, "4. Subscription", sectionFont);
		if (customer.getSubscription() != null) {
			PdfPTable subTable = createTwoColTable();
			addRow(subTable, "Subscription Type", customer.getSubscription().getSubscriptionType() != null
					? customer.getSubscription().getSubscriptionType().name() : "N/A",         labelFont, valueFont);
			addRow(subTable, "Start Date",         customer.getSubscription().getPlanStartDate() != null
					? customer.getSubscription().getPlanStartDate().toString() : "N/A",        labelFont, valueFont);
			addRow(subTable, "Expiry Date",        customer.getSubscription().getPlanExpiryDate() != null
					? customer.getSubscription().getPlanExpiryDate().toString() : "N/A",       labelFont, valueFont);
			document.add(subTable);
		} else {
			document.add(new Paragraph("No active subscription.", valueFont));
		}

		addHorizontalLine(document);
		Paragraph footer = new Paragraph("This document is system-generated.", smallFont);
		footer.setAlignment(Element.ALIGN_CENTER);
		footer.setSpacingBefore(6f);
		document.add(footer);

		document.close();
		logger.info("Customer PDF generated successfully for customerId={}", customerId);
		return new ByteArrayInputStream(out.toByteArray());
	}

	private void addHorizontalLine(Document document) {
		try {
			LineSeparator ls = new LineSeparator(1f, 100f, Color.LIGHT_GRAY, Element.ALIGN_CENTER, -2);
			Paragraph line = new Paragraph(new Chunk(ls));
			line.setSpacingAfter(8f);
			document.add(line);
		} catch (Exception e) {
			logger.warn("Could not draw horizontal line: {}", e.getMessage());
		}
	}

	private void addSectionHeading(Document document, String text, Font font) {
		try {
			Paragraph heading = new Paragraph(text, font);
			heading.setSpacingBefore(14f);
			heading.setSpacingAfter(6f);
			document.add(heading);
		} catch (Exception e) {
			logger.error("Error adding section heading", e);
		}
	}

	private PdfPTable createTwoColTable() {
		PdfPTable table = new PdfPTable(new float[]{2f, 3f});
		table.setWidthPercentage(100);
		table.setSpacingBefore(4f);
		table.setSpacingAfter(8f);
		return table;
	}

	private void addRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
		labelCell.setBorder(Rectangle.BOX);
		labelCell.setPadding(6f);
		labelCell.setBackgroundColor(new Color(240, 240, 240));
		table.addCell(labelCell);

		PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
		valueCell.setBorder(Rectangle.BOX);
		valueCell.setPadding(6f);
		table.addCell(valueCell);
	}

	private void addRow(PdfPTable table, String c1, String c2, String c3, String c4, Font labelFont, Font valueFont) {
		for (String val : new String[]{c1, c2, c3, c4}) {
			PdfPCell cell = new PdfPCell(new Phrase(val != null ? val : "N/A", valueFont));
			cell.setBorder(Rectangle.BOX);
			cell.setPadding(5f);
			table.addCell(cell);
		}
	}

	private void addTableHeader(PdfPTable table, String[] headers) {
		Font whiteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, whiteFont));
			cell.setBorder(Rectangle.BOX);
			cell.setPadding(6f);
			cell.setBackgroundColor(new Color(33, 97, 140));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
		}
	}
}