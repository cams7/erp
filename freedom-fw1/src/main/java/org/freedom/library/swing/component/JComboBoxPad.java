/**
 * @version 04/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)JComboBoxPad.java <BR>
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
 * Campo do tipo combobox.
 */

package org.freedom.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.EventListenerList;

import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.Campo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

//public class JComboBoxPad<S, T> extends JComboBox implements JComboBoxListener, ItemListener {

public class JComboBoxPad extends JComboBox implements JComboBoxListener, ItemListener, Campo {

	private static final long serialVersionUID = 1L;

	public static final int TP_NONE = -1;

	public static final int TP_STRING = 0;

	public static final int TP_INTEGER = 4;

	public static final int TP_COR = 5;

	private Vector<?> valores = new Vector<Object>();
	
	private Vector<?> labels = new Vector<Object>();

	private JComboBoxListener cbLis = this;

	private ListaCampos lcCombo = null;

	private boolean criando = true;
	
	private int tipoCampo = JTextFieldPad.TP_NONE;

	private int tam = 8;

	private int dec = 0;

	private boolean bZeroNull = false;

	private String nomecampo;

	private String nomecampolabel;

	private String tabelaexterna;

	private String whereadic;

	private String orderby;

	private ComboBoxEditor editor;
	
	private int iMascara = -1;
	
	//private boolean carregando = false;

	public void setZeroNulo() {

		bZeroNull = true;
	}

	public JComboBoxPad(Vector<?> label, Vector<?> val, int tipo, int tam, int dec) {

		criando = true;
		
		if(tipo == TP_COR) {
			
			setEditable(true);
			
			Color cor = Color.WHITE;
			
			if( (Integer) getSelectedItem() != null) {
				cor = new Color( (Integer) val.get( (Integer) getSelectedItem()) );
			}
			
			editor = new ColorComboBoxEditor(cor);

			setEditor(editor);
			
			setRenderer(new ColorCellRenderer());
			
			valores = val;
			
			for (int i = 0; i < label.size(); i++) {
				
				addItem(label.elementAt(i));
				
			}	
			
		}
		
		else if (val != null && label != null) {
			valores = val;
		
			for (int i = 0; i < label.size(); i++) {
					
					addItem(label.elementAt(i));
					
			}
		}

		labels = label;
		
		
		addItemListener(this);
	
		this.tipoCampo = tipo;
		
		this.tam = tam;
		
		this.dec = dec;
		
		criando = false;
		
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));

		

	}

	public void setItens(Vector<String> label, Vector<Object> val) {

		criando = true;
		removeAllItems();
		valores = val;
		labels = label;
		
		for (int i = 0; i < label.size(); i++) {
			addItem(label.elementAt(i));
		}
		criando = false;
			
	}

	class ColorCellRenderer implements ListCellRenderer {
		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,	isSelected, cellHasFocus);

			if (value instanceof Color && tipoCampo==TP_COR ) {
				
				if( value!=null ) {
					renderer.setBackground((Color) value );
				}
				
				renderer.setText(" "); 
				
			}

			return renderer;
		}
	}
	

	class ColorComboBoxEditor implements ComboBoxEditor {
		  final protected JButton editor;

		  protected EventListenerList listenerList = new EventListenerList();

		  public ColorComboBoxEditor(Color initialColor) {
			  
		    editor = new JButton("");
		    editor.setBackground(initialColor);
		    
		    /*
		    ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        Color currentBackground = editor.getBackground();

		        Color color = JColorChooser.showDialog(editor, "Color Chooser", currentBackground);
		        if ((color != null) && (currentBackground != color)) {
		          editor.setBackground(color);
		          fireActionEvent(color);
		        }
		        
		        
		      }
		    };
		    
		    editor.addActionListener(actionListener);
		    
		    */
		    
		  }

		  public void addActionListener(ActionListener l) {
		    listenerList.add(ActionListener.class, l);
		  }

		  public Component getEditorComponent() {
		    return editor;
		  }

		  public Object getItem() {
		    return editor.getBackground();
		  }

		  public void removeActionListener(ActionListener l) {
		    listenerList.remove(ActionListener.class, l);
		  }

		  public void selectAll() {
		    // Ignore
		  }

		  public void setItem(Object newValue) {
		    if (newValue instanceof Color) {
		    	
		      Color color = (Color) newValue;
		      editor.setBackground(color);
		      
		    } 
		    else {
		    	
		    	//setVlrString("teste");
		    	
		      try {
		     //   Color color = Color.decode(newValue.toString());
		     //   editor.setBackground(color);
		      } 
		      catch (NumberFormatException e) {
		    	  e.printStackTrace();
		      }
		    }
		  }
 
		  
		//  public void setItem(Object newvalue) {
//			  super.
		//  }
		  
		  protected void fireActionEvent(Color color) {
		    Object listeners[] = listenerList.getListenerList();
		    for (int i = listeners.length - 2; i >= 0; i -= 2) {
		      if (listeners[i] == ActionListener.class) {
		        ActionEvent actionEvent = new ActionEvent(editor, ActionEvent.ACTION_PERFORMED, color
		            .toString());
		        ((ActionListener) listeners[i + 1]).actionPerformed(actionEvent);
		      }
		    }
		  }
		}

	public void setItensGeneric(Vector<?> label, Vector<?> val) {

		criando = true;
		removeAllItems();
		valores = val;

		for (int i = 0; i < label.size(); i++) {
			addItem(label.elementAt(i));
		}
		criando = false;
	}

	public void setAtivo(boolean bVal) {

		setEnabled(bVal);
	}

	public void limpa() {

		if (getItemCount() > 0) {
			setSelectedIndex(0);
		}
	}

	public int getTipo() {

		return tipoCampo;
	}

	public int getTam() {

		return tam;
	}

	public int getDec() {

		return dec;
	}

	public String getVlrString() {

		int iInd = getSelectedIndex();
		if (valores != null && iInd >= 0 && iInd < valores.size()) {
			return valores.elementAt(getSelectedIndex()).toString();
		}
		return "";
	}
	
	public String getLabel() {

		int iInd = getSelectedIndex();
		if (labels != null && iInd >= 0 && iInd < labels.size()) {
			return labels.elementAt(getSelectedIndex()).toString();
		}
		return "";
	}



	public String getText() {

		String retorno = "";
		int iInd = getSelectedIndex();
		if (valores != null && iInd >= 0 && iInd < valores.size()) {
			retorno = valores.elementAt(getSelectedIndex()).toString();
		}
		return retorno;
	}

	public Integer getVlrInteger() {

		try {
			if (( Integer ) valores.elementAt(getSelectedIndex()) == new Integer(0) && bZeroNull) {
				return null;
			}
			return ( Integer ) valores.elementAt(getSelectedIndex());
		}
		catch (Exception err) {
			return new Integer(0);
		}
	}

	/*public void setVlrString(String val) {
		this.setVlrString(val, true, this);
	}
	*/
	public void setVlrString(String val) {
		boolean editevent = true;
		if (lcCombo!=null) {
			editevent = lcCombo.isCanedit();
		}
		for (int i = 0; i < valores.size(); i++) {
			if (valores.elementAt(i).equals(val)) {
				setSelectedIndex(i);
				fireValorAlterado(i, editevent);
				break;
			}
		}
	}

/*	public void setVlrInteger(Integer val) {
		this.setVlrInteger(val, true, this);
	}
	*/
	public void setVlrInteger(Integer val) {
		boolean editevent = true;
		if (lcCombo!=null) {
			editevent = lcCombo.isCanedit();
		}
		for (int i = 0; i < valores.size(); i++) {
			if (valores.elementAt(i).equals(val)) {
				setSelectedIndex(i);
				fireValorAlterado(i, editevent);
				break;
			}
		}
	}

	public void setListaCampos(ListaCampos lc) {

		lcCombo = lc;
	}

	public void addComboBoxListener(JComboBoxListener cb) {

		cbLis = cb;
	}

	private void fireValorAlterado(int ind, boolean editevent) {
		cbLis.valorAlterado(new JComboBoxEvent(this, ind, editevent));
	}

	public void valorAlterado(JComboBoxEvent cbevt) {

		if (( !criando ) && ( lcCombo != null )) {
		//		setCarregando(false);
			if ( lcCombo.getStatus() == ListaCampos.LCS_SELECT   && cbevt.isEditable() ) {
				//Tratamento para evitar problemas no carregamento
				//System.out.println("Object: "+cbevt.getSource().getClass().getName());
				lcCombo.edit();
			}
		}
	}

	public void itemStateChanged(ItemEvent itevt) {

		boolean editevent = true;
		// Evitar null point exception quanto não tiver amarração com listaCampos.
		if (lcCombo!=null) {
			editevent = lcCombo.isCanedit();
			if (editevent) {
				if (lcCombo.getMaster()!=null) {
					editevent = lcCombo.getMaster().isCanedit();
				}
			}
			
		}
		if (itevt.getStateChange() == ItemEvent.SELECTED) {
			fireValorAlterado(getSelectedIndex(), editevent);
		}
		
	}

	public String getNomecampo() {
		return nomecampo;
	}

	public void setNomecampo(String nomecampo) {
		this.nomecampo = nomecampo;
	}

	public String getNomecampolabel() {
		return nomecampolabel;
	}

	public void setNomecampolabel(String nomecampolabel) {
		this.nomecampolabel = nomecampolabel;
	}

	public String getTabelaexterna() {
		if (tabelaexterna!=null) {
			return tabelaexterna.toLowerCase();
		}
		return tabelaexterna;
	}

	public void setTabelaexterna(String tabelaexterna) {
		this.tabelaexterna = tabelaexterna;
	}

	public String getWhereadic() {
		return whereadic;
	}

	public void setWhereadic(String whereadic) {
		this.whereadic = whereadic;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public void carregaValores() {
		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		Vector<Object> values = new Vector<Object>();
		Vector<String> labels = new Vector<String>();
		DbConnection con = null;

		try {

			con = Aplicativo.getInstace().getConexao();

			if (con != null) {

				sql.append("select ");
				sql.append(getNomecampo());
				sql.append(",");
				sql.append(getNomecampolabel());

				sql.append(" from ");
				sql.append(getTabelaexterna());
				sql.append(" where codemp=? and codfilial=?");

				if (getWhereadic() != null) {
					sql.append(" and " + getWhereadic());
				}

				if (getOrderby() != null) {
					sql.append(" order by " + getOrderby());
				}

				ps = con.prepareStatement(sql.toString());

				int param = 1;

				ps.setInt(param++, Aplicativo.iCodEmp);
				ps.setInt(param++, ListaCampos.getMasterFilial(getTabelaexterna()));

				rs = ps.executeQuery();

				ResultSetMetaData metadata = rs.getMetaData();

				boolean first = true;

				while (rs.next()) {
					if (metadata.getColumnType(1) == Types.INTEGER) {
						if (first) {
							values.add(new Integer(-1));
						}
						values.add(new Integer(rs.getInt(1)));
					}
					else {
						if (first) {
							values.add("-1");
						}
						values.add(rs.getString(( 1 )));
					}
					if (first) {
						labels.add("<Selecione>");
					}
					labels.add(rs.getString(2));

					first = false;
				}

				rs.close();
				con.commit();
				ps.close();

				setItens(labels, values);

			}
			else {
				System.out.println("Conexão nula no combobox!");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setAutoSelect(String nomecampo, String nomecampolabel, String tabelaexterna) {

		setNomecampo(nomecampo);
		setNomecampolabel(nomecampolabel);
		setTabelaexterna(tabelaexterna);

	}

	public ListaCampos getTabelaExterna() {
		return null;
	}
	
	public String getStrMascara() {
		return "";
	}

	public int getTamanho() {
		return tam;
	}

	public void setTamanho(int tam) {
		this.tam = tam;
	}
	
	public int getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(int tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public void setDecimal(int dec) {
		this.dec = dec;
	}
	
	public int getDecimal() {
		return this.dec;
	}
	
	public int getIMascara() {
		return iMascara;
	}

	public void setIMascara(int iMascara) {
		this.iMascara = iMascara;
	}

	public ListaCampos getListaCampos() {
		return this.lcCombo;
	}

	public int getMascara() {
		return iMascara;
	}

	public void setVlrBigDecimal(BigDecimal vlr) {
		
	}

	public void cancelaDLF2() {
	//	runDLF2 = false;
	}
	
	/*public boolean isCarregando() {
		return carregando;
	}

	public void setCarregando(boolean carregando) {
		this.carregando = carregando;
	}
*/
}
