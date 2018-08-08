package de.jcup.eclipse.commons.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class AbstractPreferenceValueConverter<T> {
	
	public String convertListTostring(List<T> list){
		StringBuilder sb = new StringBuilder();
		for (T element: list){
			String converted = convertOneToString(element);
			sb.append(converted);
			sb.append(getListDelimeter());
		}
		return sb.toString();
	}
	
	public List<T> convertStringToList(String data){
		List<T> list = new ArrayList<>();
		if (data==null || data.isEmpty()){
			return list;
		}
		String[] delimiteds = data.split("\\"+getListDelimeter());
		for (String delimited: delimiteds){
			T converted = convertOneFromString(delimited);
			if (converted!=null){
				list.add(converted);
			}
		}
		return list;
	}
	
	protected class DataWriterImpl implements PreferenceDataWriter{
		private List<String> data = new ArrayList<>();
		
		public void writeInt(int integer){
			data.add(""+integer);
		}
		public void writeString(String string){
			if (string==null){
				string="";
			}
			data.add(string);
		}
		
		public String toSingleString() {
			StringBuilder sb = new StringBuilder();
			for (Iterator<String> it = data.iterator();it.hasNext();){
				sb.append(it.next());
				if (it.hasNext()){
					sb.append(getValueDelimeter());
				}
			}
			return sb.toString();
		}
	}
	
	protected class DataReaderImpl implements PreferenceDataReader{
		private List<String> data = new ArrayList<>();
		private Iterator<String> iterator;
		public DataReaderImpl(String string){
			String[] dataArray = string.split("\\"+getValueDelimeter());
			for (String dataArrayPart: dataArray){
				data.add(dataArrayPart);
			}
			iterator = data.iterator();
		}
		public int readInt() {
			String asString = readString();
			if (asString==null){
				return -1;
			}
			try{
				return Integer.parseInt(asString);
				
			}catch(NumberFormatException e){
				return -1;
			}
		}
		public String readString() {
			if (iterator.hasNext()){
				return iterator.next();
			}
			return null;
		}
	}
	/**
	 * Implementation to write entry data to string by given writer. <b>Must be same ordering as on read impl!</b>
	 * @param oneEntry
	 * @param writer
	 */
	protected abstract void write(T oneEntry, PreferenceDataWriter writer);
	/**
	 * Implementation to create and read an entry from string by given reader. <b>Must be same ordering as on write impl!</b>
	 * @param reader
	 * @return object
	 */
	protected abstract T read(PreferenceDataReader reader) ;
	
	protected String getListDelimeter() {
		return "|";
	}
	
	protected String getValueDelimeter() {
		return ",";
	}

	protected final String convertOneToString(T oneEntry){
		DataWriterImpl context = new DataWriterImpl();
		write(oneEntry,context);
		return context.toSingleString();
	}
	
	protected final T convertOneFromString(String oneEntry){
		DataReaderImpl context = new DataReaderImpl(oneEntry);
		return read(context);
	}
	

}
