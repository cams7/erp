/**
 * @version 01/02/2001 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)Tabela.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tabela tipo grid...
 */

package org.freedom.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.type.StringCentro;
import org.freedom.library.type.StringDireita;

public class JTablePad extends JTable implements TabelaEditListener, TabelaSelListener {

	// Vetor de cores de background
	private Vector<Color> vcoresb = new Vector<Color>();

	// Vetor de cores de foreground
	private Vector<Color> vcoresf = new Vector<Color>();

	private static final long serialVersionUID = 1L;

	private Modelo modelo = new Modelo();
	
	private int iDecimal = 2;

	public int getiDecimal() {
		return iDecimal;
	}

	public void setiDecimal(int iDecimal) {
		this.iDecimal = iDecimal;
	}

	int ContaColunas = 0;

	int ContaLinhas = 0;

	boolean bAutoRol = false;

	TabelaEditListener edlis = this;

	List<TabelaSelListener> seLis = new ArrayList<TabelaSelListener>();

	public JTablePad() {

		setModel(modelo);

		setCursor(new Cursor(Cursor.HAND_CURSOR));

		setAutoResizeMode(AUTO_RESIZE_OFF);

		setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		
		//addKeyListener(this);

		// Evitando que o ENTER no grid mude para a linha abaixo
		InputMap im =  getInputMap();
		ActionMap am =  getActionMap();
	
		im.clear();
		am.clear();
		
		Action enterKey = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				try	{					
					simulaDuploClique();
				}
				catch (Exception ex){
					ex.printStackTrace();
				}					
			}
		};
		
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "SimulaDuploClick");
		am.put("SimulaDuploClick", enterKey);
	
		
		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo BigDecimal
		 * 
		 *******************************************************/
		DefaultTableCellRenderer bigDecimalRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {

				setText(( value == null ) ? "" : "" + value);
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {
					
					setBackground(new Color(160,170,210));
					
				}
				else {

					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {						
							setBackground(new Color(210,215,220));
						}
					} 
					else {
						
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {
							setBackground(null);
						}
						
					}
					
				}
				
				setText( Funcoes.getStringFormatedBigDecimal( (BigDecimal) value , iDecimal));
				
				return this;
			}

		};

		bigDecimalRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		setDefaultRenderer(BigDecimal.class, bigDecimalRenderer);

		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo Integer
		 * 
		 *******************************************************/
		
		DefaultTableCellRenderer intDecimalRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {

				setText(( value == null ) ? "" : "" + value);
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {

					setBackground(new Color(160,170,210));

				}
				else {

					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column) );
						}
						else {

							setBackground(new Color(210,215,220));
						}
					} 
					else {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(null);
						}
					}
				}
				return this;
			}

		};

		intDecimalRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		setDefaultRenderer(Integer.class, intDecimalRenderer);

		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo StringDireita
		 * 
		 *******************************************************/
		
		DefaultTableCellRenderer stringDireitaRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {

				setText(( value == null ) ? "" : "" + value);
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {
					
					setBackground(new Color(160,170,210));
					
				}
				else {
					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(new Color(210,215,220));
						}
					} 
					else {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(null);
						}
					}
				}

				return this;
			}

		};
		
		stringDireitaRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		setDefaultRenderer(StringDireita.class, stringDireitaRenderer);
		
		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo StringCentro
		 * 
		 *******************************************************/
		
		DefaultTableCellRenderer stringCentroRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {

				setText(( value == null ) ? "" : "" + value.toString()); 
				
				
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {
					
					setBackground(new Color(160,170,210));
					
				}
				else {
					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(new Color(210,215,220));
						}
					} 
					else {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(null);
						}
					}
				}

				setText( value.toString() );
				
				return this;
			}

		};
		
		stringCentroRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		setDefaultRenderer(StringCentro.class, stringCentroRenderer);

		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo Imagem
		 * 
		 *******************************************************/
		
		DefaultTableCellRenderer imageIconRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value)	{ 
			
				if (value != null)	{
					setIcon((ImageIcon)value);
				}
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {
					setBackground(new Color(160,170,210));
				}
				else {
					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(new Color(210,215,220));
						}
					} 
					else {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(null);
						}
					}
				}

				return this;
			}

		};
		
		imageIconRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		setDefaultRenderer(ImageIcon.class, imageIconRenderer);
		
		
		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo Color
		 * 
		 *******************************************************/
		
		DefaultTableCellRenderer ColorRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value)	{ 
			
				if (value != null)	{
//					Icon((ImageIcon)value);
					setBackground((Color) value);
//					setText(( value == null ) ? "" : "Cor" );
					
					
				}
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				setBackground((Color) value);

				return this;
			}

		};
		
		ColorRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		setDefaultRenderer(Color.class, ColorRenderer);
		
		
		
		/*******************************************************
		 * 
		 * Configuração visual para valores do tipo Data
		 * 
		 *******************************************************/
		
		DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {

				setText(( value == null ) ? "" : "" + ConversionFunctions.dateToStrDate((Date)value));
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {

					setBackground(new Color(160,170,210));

				}
				else {

					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column) );
						}
						else {

							setBackground(new Color(210,215,220));
						}
					} 
					else {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(null);
						}
					}
				}
				return this;
			}

		};

		dateRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		setDefaultRenderer(Date.class, dateRenderer);
		
		
		/*******************************************************
		 * 
		 * Configuração visual para demais tipos de dados
		 * 
		 *******************************************************/
		
		setDefaultRenderer ( Object.class, new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(isSelected) {
					setBackground(new Color(160,170,210));
				}
				else {

					if(row % 2 == 0) {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(new Color(210,215,220));
						}
					} 
					else {
						if(modelo.getColorAt(row, column)!=null) {
							setBackground(modelo.getColorAt(row, column));
						}
						else {

							setBackground(null);
						}
					}
				}

				return this;
			}

		});

		setDefaultEditor(Date.class, new DateEditor());

	}

	public void adicColuna(Object obj) {

		modelo.addColumn(obj);
		ContaColunas++;
	}

	public void tiraColuna(int ind) {

		if (ind < ContaColunas) {
			removeColumn(getColumnModel().getColumn(ind));
		}
		ContaColunas--;
	}

	public int getNumColunas() {

		return ContaColunas;
	}

	public void setTamColuna(int tam, int ind) {

		if (ind < ContaColunas)
			getColumnModel().getColumn(ind).setPreferredWidth(tam);
	}

	public void addTabelaEditListener(TabelaEditListener tl) {

		edlis = tl;
		modelo.setTabelaEditListenerSrc(this);
	}

	public void addTabelaSelListener(TabelaSelListener tl) {

		if (tl != null) {
			seLis.add(tl);
		}
	}

	public void removeTabelaSelListener(TabelaSelListener tl) {

		if (tl != null) {
			seLis.remove(tl);
		}
	}

	public void adicLinha() {

		Vector<Object> vVals = new Vector<Object>();
		
		for (int i = 0; i < ContaColunas; i++) {
			vVals.addElement("");
		}
		
		modelo.addRow(vVals);
		
		
		if (bAutoRol) {
			setLinhaSel(ContaLinhas);
		}
		ContaLinhas++;
	}

	public void adicLinha(Vector<Object> valores) {

		modelo.addRow(valores);
		if (bAutoRol) {
			setLinhaSel(ContaLinhas);
		}
		ContaLinhas++;
	}

	public void adicLinha(Object[] valores) {

		modelo.addRow(valores);
		if (bAutoRol) {
			setLinhaSel(ContaLinhas);
		}
		ContaLinhas++;
	}

	public void setColunaInvisivel(int icol) {
		this.getColumnModel().getColumn(icol).setMaxWidth(0);
		this.getColumnModel().getColumn(icol).setMinWidth(0);
		this.getTableHeader().getColumnModel().getColumn(icol).setMaxWidth(0);
		this.getTableHeader().getColumnModel().getColumn(icol).setMinWidth(0);
	}

	public void setColunaVisivel(int tam, int icol) {
		this.getColumnModel().getColumn(icol).setMaxWidth(tam);
		this.getColumnModel().getColumn(icol).setMinWidth(tam);
		this.getTableHeader().getColumnModel().getColumn(icol).setMaxWidth(tam);
		this.getTableHeader().getColumnModel().getColumn(icol).setMinWidth(tam);
	}

	public int pesqLinha(int iCol, String sTexto) {

		int iRetorno = -1;
		int iTam = 0;
		Vector<?> vTemp = null;
		try {
			if (sTexto != null) {
				iTam = sTexto.length();
				if (iTam > 0) {
					for (int i = 0; i < getNumLinhas(); i++) {
						vTemp = getLinha(i);
						if (( ( String ) vTemp.elementAt(iCol) ).substring(0, iTam).equals(sTexto)) {
							iRetorno = i;
							break;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			vTemp = null;
		}
		return iRetorno;
	}

	public void adicLinha(int iLin, Vector<Object> vDados) {

		modelo.insertRow(iLin, vDados);
		ContaLinhas++;
	}

	public void tiraLinha(int ind) {
		if (ind < ContaLinhas && ind >= 0) {
			boolean bSel = false;
			if (getSelectedRow() > -1) {
				bSel = ( convertRowIndexToModel(getSelectedRow()) == ind );
			}
			else {
				bSel = false;
			}

			modelo.removeRow(ind);

			if (bSel && ind < ( getNumLinhas() - 1 ))
				setLinhaSel(ind);
		}
		ContaLinhas--;
	}

	public Vector<Object> getLinha(int iInd) {

		return modelo.getRow(iInd);
	}

	public int getNumLinhas() {

		return ContaLinhas;
	}

	public void setValor(Object obj, int lin, int col) {

		if (obj == null)
			obj = "";
		if (( lin < ContaLinhas ) & ( col < ContaColunas )) {
			modelo.setValueAt(obj, lin, col);
			modelo.setColorAt(null, lin, col);
		}
	}
	
	public void setValor(Object obj, int lin, int col, Color back) {

		if (obj == null)
			obj = "";
		if (( lin < ContaLinhas ) & ( col < ContaColunas )) {
			modelo.setValueAt(obj, lin, col);
			modelo.setColorAt(back, lin, col);
		}
		
	}


	public Object getValor(int lin, int col) {

		Object ret = null;
		if (( lin < ContaLinhas ) & ( col < ContaColunas )) {
			ret = modelo.getValueAt(lin, col);
		}
		return ret;
	}
	
	public Color getColor(int lin, int col) {

		Color ret = null;
		if (( lin < ContaLinhas ) & ( col < ContaColunas )) {
			ret = (Color) modelo.getColorAt(lin, col);
		}
		return ret;
	}


	public int getLinhaSel() {
		if (getSelectedRow() > -1) {
			return convertRowIndexToModel(getSelectedRow());
		}
		else {
			return -1;
		}
	}

	public int getColunaEditada() {

		return getEditingColumn();
	}

	public int getLinhaEditada() {

		return getEditingRow();
	}

	public void setLinhaSel(int lin) {
		try {
			if (lin < 1) {
				setRowSelectionInterval(0, 0);
			}
			else {
				setRowSelectionInterval(lin - 1, lin);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setEditavel(boolean bEdit) {

		modelo.Editavel(bEdit);
	}

	public void limpa() {
		modelo.limpa();
		ContaLinhas = 0;
	}

	public void limpaTudo() {

		limpa();

		while (columnModel.getColumnCount() > 0) {
			TableColumn col = columnModel.getColumn(0);
			columnModel.removeColumn(col);
		}
	}

	public void setColunaEditavel(int iCol, boolean bVal) {

		modelo.ColunaEditavel(iCol, bVal);
	}

	public void setAutoRol(boolean bVal) {

		bAutoRol = bVal;
	}

	public void delLinha(int iLinha) {

		modelo.removeRow(iLinha);
		ContaLinhas--;
	}

	public void setRowBackGround(int iLinha, Color cor) {

		if (iLinha < ContaLinhas) {
			for (int i = 0; i < ContaColunas; i++) {
				( ( DefaultTableCellRenderer ) getCellRenderer(iLinha, i) ).setBackground(cor);
			}
		}
	}

	public void setColColor(int iLinha, int iCol, Color fundo, Color frente) {
		try {
			TableColumn tm = this.getColumnModel().getColumn(iCol);
			tm.setCellRenderer(new ColorColumnRenderer(fundo, frente, iLinha));
		}
		catch (Exception e) {
			System.out.println("Coluna inexistente.");
		}
	}

	class ColorColumnRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		// private Color corfundo;
		// private Color corfrente;
		int crow;

		public ColorColumnRenderer(Color fundo, Color frente, int prow) {
			super();
			// corfundo = fundo;
			// corfrente = frente;
			crow = prow;
			vcoresb.addElement(fundo);
			vcoresf.addElement(frente);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			// int icor1 = ((Color) vcoresb.elementAt( row )).getRGB();
			// int icor2 = ((Color) vcoresf.elementAt( row )).getRGB();

			cell.setBackground(( Color ) vcoresb.elementAt(row));
			cell.setForeground(( Color ) vcoresf.elementAt(row));

			return cell;
		}
	}

	public void valueChanged(ListSelectionEvent levt) {

		if (seLis != null && getLinhaSel() >= 0) {
			fireValorSelAlterado();
		}

		super.valueChanged(levt);
	}

	public void fireValorEditAlterado() {

		edlis.valorAlterado(new TabelaEditEvent(this));
	}

	public void fireValorSelAlterado() {

		for (TabelaSelListener tl : seLis) {
			tl.valorAlterado(new TabelaSelEvent(this));
		}
	}

	public void valorAlterado(TabelaEditEvent tevt) {

	}

	public void valorAlterado(TabelaSelEvent tevt) {

	}
	
	public Vector<Vector<Object>> getDataVector() {
		if (modelo!=null) {
			return modelo.getDataVector();
		} else {
			return null;
		}
	}

	public void setDataVector(Vector<Vector<Object>> datavector) {
		limpa();
		for (Vector<Object> row: datavector ) {
			adicLinha(row);
		}
	}
	
	public Vector<Object> getColumnNames() {
		Vector<Object> columnNames = new Vector<Object>();
		if (this.modelo!=null) {
			for (int i=0; i<modelo.getColumnCount(); i++) {
				columnNames.addElement(modelo.getColumnName(i));
			}
		}
		return columnNames;
	}

	
	class Modelo extends AbstractTableModel implements Serializable {

		private static final long serialVersionUID = 1L;

		protected Vector<Vector<Object>> dataVector;
		
		protected Vector<Vector<Object>> colorVector;

		protected Vector<Object> columnIdentifiers;

		boolean bEditavel = false;

		boolean[] bColsEdit = null;

		JTablePad tabEdLis = null;
		
		public Modelo() {

			this(( Vector<Object> ) null, 0);
		}

		public Modelo(int numRows, int numColumns) {

			Vector<Object> names = new Vector<Object>(numColumns);
			names.setSize(numColumns);
			setColumnIdentifiers(names);
			dataVector = new Vector<Vector<Object>>();
			colorVector = new Vector<Vector<Object>>();
			setNumRows(numRows);
		}

		public Modelo(Vector<Object> columnNames, int numRows) {

			setColumnIdentifiers(columnNames);
			dataVector = new Vector<Vector<Object>>();
			colorVector = new Vector<Vector<Object>>();
			setNumRows(numRows);
		}

		public Modelo(Vector<Vector<Object>> data, Vector<Object> columnNames) {

			setDataVector(data, columnNames);
		}

		public Modelo(Object[][] data, Object[] columnNames) {

			setDataVector(data, columnNames);
		}

		public void setTabelaEditListenerSrc(JTablePad tab) {

			tabEdLis = tab;
		}

		public Vector<Vector<Object>> getDataVector() {

			return dataVector;
		}

		public void setDataVector(Vector<Vector<Object>> newData, Vector<Object> columnNames) {

			if (newData == null)
				throw new IllegalArgumentException("setDataVector() - Null parameter");
			dataVector = new Vector<Vector<Object>>(0);
			setColumnIdentifiers(columnNames);
			dataVector = newData;
			newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		}

		public void setDataVector(Object[][] newData, Object[] columnNames) {

			setDataVector(convertToVector(newData), convertToVector(columnNames));
		}

		public void newDataAvailable(TableModelEvent event) {

			fireTableChanged(event);
		}

		public void newRowsAdded(TableModelEvent event) {

			int start = event.getFirstRow();
			int end = event.getLastRow();
			if (start < 0)
				start = 0;
			if (end < 0)
				end = getRowCount() - 1;
			for (int i = start; i < end; i++)
				dataVector.elementAt(i).setSize(getColumnCount());
			fireTableChanged(event);
		}

		public void rowsRemoved(TableModelEvent event) {

			fireTableChanged(event);
		}

		public void setColumnIdentifiers(Vector<Object> newIdentifiers) {

			if (newIdentifiers != null) {
				columnIdentifiers = newIdentifiers;
			}
			else {
				columnIdentifiers = new Vector<Object>();
			}
			fireTableStructureChanged();
		}

		public void setColumnIdentifiers(Object[] newIdentifiers) {

			setColumnIdentifiers(convertToVector(newIdentifiers));
		}

		public void setNumRows(int newSize) {

			if (( newSize < 0 ) || ( newSize == getRowCount() ))
				return;
			int oldNumRows = getRowCount();
			if (newSize <= getRowCount()) {
				dataVector.setSize(newSize);
				colorVector.setSize(newSize);
				fireTableRowsDeleted(getRowCount(), oldNumRows - 1);
			}
			else {
				int columnCount = getColumnCount();
				while (getRowCount() < newSize) {
					Vector<Object> newRow = new Vector<Object>(columnCount);
					Vector<Object> newRow2 = new Vector<Object>(columnCount);
					
					newRow.setSize(columnCount);
					newRow2.setSize(columnCount);
					dataVector.addElement(newRow);
					colorVector.addElement(newRow2);
				}
				fireTableRowsInserted(oldNumRows, getRowCount() - 1);
			}
		}

		public void addColumn(Object columnName) {

			addColumn(columnName, ( Vector<Object> ) null);
		}

		public void addColumn(Object columnName, Vector<?> columnData) {

			if (columnName == null)
				throw new IllegalArgumentException("addColumn() - null parameter");
			columnIdentifiers.addElement(columnName);
			int index = 0;
			Enumeration<Vector<Object>> enumeration = dataVector.elements();
			while (enumeration.hasMoreElements()) {
				Object value;
				if (( columnData != null ) && ( index < columnData.size() ))
					value = columnData.elementAt(index);
				else
					value = null;
				enumeration.nextElement().addElement(value);
				index++;
			}
			
			Enumeration<Vector<Object>> enumeration2 = colorVector.elements();
			index = 0;
			while (enumeration2.hasMoreElements()) {
				Object value;
				if (( columnData != null ) && ( index < columnData.size() ))
					value = columnData.elementAt(index);
				else
					value = null;
				enumeration2.nextElement().addElement(value);
				index++;
			}
			
			fireTableStructureChanged();
		}

		public void addColumn(Object columnName, Object[] columnData) {

			addColumn(columnName, convertToVector(columnData));
		}

		@SuppressWarnings("unchecked")
		public void addRow(Vector<Object> rowData) {
			
			if (rowData == null) {
				rowData = new Vector<Object>(getColumnCount());
				
			}
			else {
				rowData.setSize(getColumnCount());
			}
			
			Vector<Object> rowData2 = (Vector<Object>) rowData.clone();
			
			dataVector.addElement( rowData );
			colorVector.addElement( rowData2 );
			
			newRowsAdded(new TableModelEvent(this, getRowCount() - 1, getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		}

		public void addRow(Object[] rowData) {

			addRow(convertToVector(rowData));
		}

		public void insertRow(int row, Vector<Object> rowData) {

			if (rowData == null) {
				rowData = new Vector<Object>(getColumnCount());
			}
			else {
				rowData.setSize(getColumnCount());
			}
			dataVector.insertElementAt(rowData, row);
			newRowsAdded(new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		}

		public void insertRow(int row, Object[] rowData) {

			insertRow(row, convertToVector(rowData));
		}

		public void moveRow(int startIndex, int endIndex, int toIndex) {

			if (( startIndex < 0 ) || ( startIndex >= getRowCount() ))
				throw new ArrayIndexOutOfBoundsException(startIndex);
			if (( endIndex < 0 ) || ( endIndex >= getRowCount() ))
				throw new ArrayIndexOutOfBoundsException(endIndex);
			if (startIndex > endIndex)
				throw new ArrayIndexOutOfBoundsException();
			if (( startIndex <= toIndex ) && ( toIndex <= endIndex ))
				return;
			boolean shift = toIndex < startIndex;
			for (int i = startIndex; i <= endIndex; i++) {
				Vector<Object> aRow = dataVector.elementAt(i);
				dataVector.removeElementAt(i);
				dataVector.insertElementAt(aRow, toIndex);
				if (shift)
					toIndex++;
			}
			fireTableDataChanged();
		}

		public void removeRow(int row) {
			dataVector.removeElementAt(row);
			fireTableRowsDeleted(row, row);
		}

		public Vector<Object> getRow(int iInd) {

			return dataVector.elementAt(iInd);
		}

		public void limpa() {
			dataVector.removeAllElements();
			vcoresb = new Vector<Color>();
			vcoresf = new Vector<Color>();
			try {
				fireTableRowsDeleted(0, dataVector.size());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int getRowCount() {

			return dataVector.size();
		}

		public int getColumnCount() {

			return columnIdentifiers.size();
		}

		public String getColumnName(int column) {

			if (columnIdentifiers == null || columnIdentifiers.size() <= column) {
				return super.getColumnName(column);
			}
			Object id = columnIdentifiers.elementAt(column);
			if (id == null) {
				return super.getColumnName(column);
			}
			return id.toString();
		}

		public void ColunaEditavel(int iCol, boolean bVal) {

			if (bColsEdit == null) {
				bColsEdit = new boolean[getColumnCount()];
			}
			bColsEdit[iCol] = bVal;
		}

		public void Editavel(boolean bEdit) {

			bEditavel = bEdit;
		}

		public boolean isCellEditable(int row, int column) {

			boolean bRetorno = bEditavel;
			if (bColsEdit != null) {
				bRetorno = bColsEdit[column];
			}
			return bRetorno;
		}

		public Object getValueAt(int row, int column) {
			
			Object ret = null;
			Vector<?> rowVector = null;
			
			try {
				rowVector = dataVector.elementAt(row);
				ret = rowVector.elementAt(column);
			}
			catch (Exception e) {
				System.out.println("Data vector: " + dataVector);
				System.out.println("Linha:" + row + " Coluna:" + column);
				System.out.println("Dados: " + rowVector);
				//e.printStackTrace();
				
			}
	
			return ret;
			
		}

		public Color getColorAt(int row, int column) {

			Vector<?> rowVector = colorVector.elementAt(row);
			
			if(rowVector.elementAt(column) instanceof Color) {			
				return (Color) rowVector.elementAt(column);
			}
			
			return null;
			
		}

		
		public void setValueAt(Object aValue, int row, int column) {

			Vector<Object> rowVector = dataVector.elementAt(row);
			rowVector.setElementAt(aValue, column);
			fireTableChanged(new TableModelEvent(this, row, row, column));
		}
		
		public void setColorAt(Color cor, int row, int column) {

			Vector<Object> rowVector = colorVector.elementAt(row);
			rowVector.setElementAt(cor, column);
//			fireTableChanged(new TableModelEvent(this, row, row, column));
		}

		public Class<? extends Object> getColumnClass(int c) {

			Object bRet = getValueAt(0, c);
			return bRet == null ? null : bRet.getClass();
		}

		protected Vector<Object> convertToVector(Object[] anArray) {

			if (anArray == null)
				return null;
			Vector<Object> v = new Vector<Object>(anArray.length);
			for (int i = 0; i < anArray.length; i++) {
				v.addElement(anArray[i]);
			}
			return v;
		}

		protected Vector<Vector<Object>> convertToVector(Object[][] anArray) {

			if (anArray == null)
				return null;
			Vector<Vector<Object>> v = new Vector<Vector<Object>>(anArray.length);
			for (int i = 0; i < anArray.length; i++) {
				v.addElement(convertToVector(anArray[i]));
			}
			return v;
		}

		public void fireTableChanged(TableModelEvent tevt) {

			if (tabEdLis != null)
				tabEdLis.fireValorEditAlterado();
			super.fireTableChanged(tevt);

		}
	}

	
	public void simulaDuploClique() {
		try {
			
			Robot robo = new Robot();
			
		//	Rectangle retcel = getCellRect(getLinhaSel(), getNumColunas()-1, false);
			Rectangle retcel = getCellRect(getLinhaSel(), 0, false);
	
			Component pai = getParent();
			
			int y = retcel.y;
			int x = retcel.x;
/*
			if(x>retcel.x) {
				x = retcel.x;
			}
	*/		
			y += getRowHeight( ( getLinhaSel()-1 )) / 2;
			
			robo.mouseMove(x, y);
			
			
			boolean tempai = pai == null ? false : true;
			
			pai = this;
			
			while(tempai) {
				
				Component filho = pai.getParent();
				
				if(filho!=null && filho.getClass().getCanonicalName().equals(JViewport.class.getCanonicalName())){
					
					JViewport vp = (JViewport) filho ;
					
					Point vvp = vp.getViewPosition();
//					System.out.println("View Position: X:" + vvp.getX() + " Y:" +vvp.getY());
					
					y = y - (new Double( vvp.getY()).intValue()) ;
					
					//x = x - (new Double( vvp.getX()).intValue());
					
					
					x = x +  (new Double( vp.getWidth()/4).intValue());
					
				}
				
				
				if (filho==null) {
					tempai = false;
				}
				else {
					System.out.println("Classe pai:" + filho.getClass().getCanonicalName());
					y += filho.getY();
					x += filho.getX();
					pai = filho;
				}
			}
			
			//Component frame = SwingUtilities.getDeepestComponentAt(this, this.getX(), this.getY());
			
			//frame.getX(); 
			
			//x += frame.getX();
			
			robo.mouseMove(x, y);
			
			robo.mousePress(MouseEvent.BUTTON1_MASK);
			robo.mouseRelease(MouseEvent.BUTTON1_MASK);
			
			robo.mousePress(MouseEvent.BUTTON1_MASK);
			robo.mouseRelease(MouseEvent.BUTTON1_MASK);
		
			System.out.println("Click simulado! posição: X " + x + " Y:" + y);
			
			
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}
	

}

class DateEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public DateEditor() {

		super(new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0));
		delegate = new EditorDelegate() {

			private static final long serialVersionUID = 1L;

			public void setValue(Object value) {

				( ( JTextFieldPad ) editorComponent ).setVlrDate(( Date ) value);
			}
		};
	}

	public Object getCellEditorValue() {

		return ( ( JTextFieldPad ) editorComponent ).getVlrDate();
	}
}
