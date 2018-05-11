package org.archivemanager.data;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.heed.openapps.QName;
import org.heed.openapps.SystemModel;
import org.heed.openapps.dictionary.RepositoryModel;
import org.heed.openapps.entity.Association;
import org.heed.openapps.entity.AssociationImpl;
import org.heed.openapps.entity.Entity;
import org.heed.openapps.entity.EntityImpl;


public class ExcelCollectionImportProcessor extends CollectionImportProcessor {
	private static final long serialVersionUID = 9184136800231116563L;
	private final static Logger log = Logger.getLogger(ExcelCollectionImportProcessor.class.getName());
	private Map<String, Entity> categories = new HashMap<String, Entity>();
	private Map<String,String> qnameLabels;
	boolean inBold = false;
    boolean inItalic = false;
    boolean inUnderline = false;
	
	public void process(InputStream stream, Map<String, Object> properties) throws Exception {
		categories.clear();
		getEntities().clear();
		qnameLabels = getQNameLabels();

		Entity collection = new EntityImpl();
		collection.setUid(java.util.UUID.randomUUID().toString());
		collection.setQName(RepositoryModel.CATEGORY);
		setRoot(collection);
		
		XSSFWorkbook workbook = new XSSFWorkbook(stream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int count = 0;
		for(Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
			Row data = rit.next();
			if(count > 0) {	
				String series = clean(getStringCellValue(data.getCell(2)));
				String subseries = clean(getStringCellValue(data.getCell(3)));
				String genre = clean(getStringCellValue(data.getCell(4)));
				String form = clean(getStringCellValue(data.getCell(5)));
				String heading = clean(getStringCellValue(data.getCell(6)));
				String beginDate = clean(getStringCellValue(data.getCell(7)));
				String endDate = clean(getStringCellValue(data.getCell(8)));
				String dateExpression = clean(getStringCellValue(data.getCell(9)));
				String description = clean(getStringCellValue(data.getCell(10)));
				String container1Type = clean(getStringCellValue(data.getCell(12)));
				String container1Num = clean(getStringCellValue(data.getCell(13)));
				String container2Type = clean(getStringCellValue(data.getCell(14)));
				String container2Num = clean(getStringCellValue(data.getCell(15)));
				String language = clean(getStringCellValue(data.getCell(16)));
				String accessionDate = clean(getStringCellValue(data.getCell(17)));
				String notesType = clean(getStringCellValue(data.getCell(18)));
				String notesText = clean(getStringCellValue(data.getCell(19)));
				
				Entity row = new EntityImpl();
				try {
					QName qname = QName.createQualifiedName(qnameLabels.get(series));
					row.setQName(qname);
				} catch (Exception e) {
					log.log(Level.SEVERE, "", e);
				}
				row.setUid(UUID.randomUUID().toString());				
				row.setName(heading);
				row.addProperty(RepositoryModel.DESCRIPTION, description);
				//row.addProperty(RepositoryModel.SUMMARY, summary);
				row.addProperty(RepositoryModel.CONTAINER, getContainer(container1Type,container1Num,container2Type,container2Num));
				row.addProperty(RepositoryModel.LANGUAGE, language);
				row.addProperty(new QName("openapps_org_repository_1_0", "form"), form);
				row.addProperty(new QName("openapps_org_repository_1_0", "genre"), genre);
				row.addProperty(RepositoryModel.ACCESSION_DATE, accessionDate);
				row.addProperty(RepositoryModel.DATE_EXPRESSION, dateExpression);
				row.addProperty(RepositoryModel.BEGIN_DATE, beginDate);
				row.addProperty(RepositoryModel.END_DATE, endDate);
				if(notesType != null && notesType.length() > 0 && notesText != null && notesText.length() > 0) {
					Entity note = new EntityImpl(SystemModel.NOTE);
					note.addProperty(SystemModel.NOTE_TYPE, notesType);
					note.addProperty(SystemModel.NOTE_CONTENT, notesText);
				}
				
				Entity seriesCategory = null;
				seriesCategory = categories.get(series);
				if(seriesCategory == null) {
					seriesCategory = new EntityImpl(RepositoryModel.CATEGORY);
					seriesCategory.setUid(UUID.randomUUID().toString());
					seriesCategory.setName(series);	
					seriesCategory.addProperty(RepositoryModel.CATEGORY_LEVEL, "series");
					categories.put(series, seriesCategory);
					getEntities().put(String.valueOf(seriesCategory.getUid()), seriesCategory);					
				}
				Entity subseriesCategory = null;
				subseriesCategory = categories.get(subseries);
				if(subseriesCategory == null) {
					subseriesCategory = new EntityImpl(RepositoryModel.CATEGORY);
					subseriesCategory.setUid(UUID.randomUUID().toString());
					subseriesCategory.setName(subseries);
					subseriesCategory.addProperty(RepositoryModel.CATEGORY_LEVEL, "subseries");
					categories.put(subseries, subseriesCategory);
					getEntities().put(String.valueOf(subseriesCategory.getUid()), subseriesCategory);
				}
				if(!containsAssociation(seriesCategory, subseriesCategory)) {
					Association assoc = new AssociationImpl(RepositoryModel.CATEGORIES, seriesCategory, subseriesCategory);
					seriesCategory.getSourceAssociations().add(assoc);
					subseriesCategory.getTargetAssociations().add(assoc);
				}								
				Association assoc = new AssociationImpl(RepositoryModel.ITEMS, subseriesCategory, row);
				subseriesCategory.getSourceAssociations().add(assoc);
				row.getTargetAssociations().add(assoc);				
				getEntities().put(String.valueOf(row.getUid()), row);
			}
			count++;
	    }		
	}
	
	protected Map<String,String> getQNameLabels() {
		Map<String,String> labels = new HashMap<String,String>();
		labels.put("Manuscripts", "openapps_org_repository_1_0_manuscript");
		labels.put("Correspondence", "openapps_org_repository_1_0_correspondence");
		labels.put("Printed Material", "openapps_org_repository_1_0_printed_material");
		labels.put("Audio", "openapps_org_repository_1_0_audio");
		labels.put("Professional Material", "openapps_org_repository_1_0_professional");
		labels.put("Memorabilia", "openapps_org_repository_1_0_memorabilia");
		labels.put("Journals", "openapps_org_repository_1_0_journals");
		labels.put("Scrapbooks", "openapps_org_repository_1_0_scrapbooks");
		labels.put("Financial", "openapps_org_repository_1_0_financial");
		labels.put("Legal Material", "openapps_org_repository_1_0_legal");
		labels.put("Artwork", "openapps_org_repository_1_0_artwork");
		labels.put("Photographs", "openapps_org_repository_1_0_photographs");
		labels.put("Notebooks", "openapps_org_repository_1_0_notebooks");
		labels.put("Medical", "openapps_org_repository_1_0_medical");
		labels.put("Research", "openapps_org_repository_1_0_research");
		labels.put("Miscellaneous", "openapps_org_repository_1_0_miscellaneous");
		labels.put("Video", "openapps_org_repository_1_0_video");
		return labels;
	}
	protected boolean containsAssociation(Entity parent, Entity child) {
		for(Association assoc : parent.getSourceAssociations()) {
			if(assoc.getTargetEntity().getName().equals(child.getName()))
				return true;
		}
		return false;
	}
	protected String getContainer(String container1Type, String container1Num, String container2Type, String container2Num) {
		StringWriter writer = new StringWriter();
		if(container1Type != null && container1Type.length() > 1 && container1Num != null && container1Num.length() > 0) 
			writer.write(container1Type+" "+container1Num);
		if(container2Type != null && container2Type.length() > 1 && container2Num != null && container2Num.length() > 0) 
			writer.write(" "+container2Type+" "+container2Num);
		return writer.toString();
	}
	protected String getStringCellValue(Cell cell) {
		if(cell == null) return "";
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
        case Cell.CELL_TYPE_NUMERIC:
            if(DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf((int)cell.getNumericCellValue());
            }
        case Cell.CELL_TYPE_BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());
        default:
            return "";
		}
	}
	protected String getHtmlFormatedCellValue(XSSFCell cell) {		
		XSSFRichTextString cellText = cell.getRichStringCellValue();
        String htmlCode = "";
        int length = cellText.toString().length();        
        for (int i = 0; i < length; i++) {
        	try {
        		XSSFFont font = cellText.getFontAtIndex(i);
        		if(font != null) htmlCode += getFormatFromFont(font);
        	} catch(Exception e) {
        		//e.printStackTrace();
        	}
        	String character = Character.toString(cellText.getString().charAt(i));
        	if(!character.equals("\r")) htmlCode += character;
        }
        if (inItalic) {
            htmlCode += "</i>";
            inItalic = false;
        }
        if (inBold) {
            htmlCode += "</b>";
            inBold = false;
        }
        if (inUnderline) {
            htmlCode += "</u>";
            inUnderline = false;
        }
        //System.out.println(htmlCode);
        return htmlCode;
    }
	protected String getFormatFromFont(XSSFFont font) {
		String formatHtmlCode = "";
        if (font.getItalic() && !inItalic) {
            formatHtmlCode += "<i>";
            inItalic = true;
        } else if (!font.getItalic() && inItalic) {
            formatHtmlCode += "</i>";
            inItalic = false;
        }
        if (font.getBold() && !inBold) {
            formatHtmlCode += "<b>";
            inBold = true;
        } else if (!font.getBold() && inBold) {
            formatHtmlCode += "</b>";
            inBold = false;
        }
        if(font.getUnderline() > 0 && !inUnderline) {        	
            formatHtmlCode += "<u>";
            inUnderline = true;
        } else if (font.getUnderline() == 0 && inUnderline) {
            formatHtmlCode += "</u>";
            inUnderline = false;
        }
        return formatHtmlCode;
    }
	protected String clean(String in) {
		return in.trim();
	}
}
