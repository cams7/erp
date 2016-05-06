/**
 * @version 17/08/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva/Robson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)GuardaCampo.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários da classe.....
 */

package org.freedom.library.persistence;

import java.awt.Color;
import java.awt.Component;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.BorderFactory;

import org.freedom.library.business.exceptions.ExceptionLimpaComponent;
import org.freedom.library.component.DadosImagem;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.PainelImagem;

public class GuardaCampo extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextFieldPad txtCampo = null;
	private Component comp = null;
	private String sNome = "";
	private String sLabel = "";
	private JTextFieldFK txtDescFK = null;
	private boolean bPK = false;
	private boolean bFK = false;
	private int iTipo = -1;
	private boolean bRequerido = false;
	private boolean bVisivel = true;
	//private boolean carregando = false;

	public GuardaCampo(Component c, String nome, String label, byte key, JTextFieldFK descFK, boolean req) {
		setGuardaCampo(c, nome, label, key, descFK, req);
	}

	public GuardaCampo(Component c, String nome, String label, byte key, JTextFieldFK descFK, boolean req, boolean bVisivel) {
		setGuardaCampo(c, nome, label, key, descFK, req, bVisivel);
	}

	public GuardaCampo(Component c, String nome, String label, byte key, boolean req) {
		setGuardaCampo(c, nome, label, key, null, req);
	}

	public void setGuardaCampo(Component c, String nome, String label, byte key, JTextFieldFK descFK, boolean req, boolean bVisivel) {
		sNome = nome;
		sLabel = label;
		this.bVisivel = bVisivel;
		comp = c;
		bRequerido = req;
		bPK = ( key == ListaCampos.DB_PK ) || ( key == ListaCampos.DB_PF );
		bFK = ( key == ListaCampos.DB_FK ) || ( key == ListaCampos.DB_PF );

		if (comp instanceof JTextFieldPad) {
			txtCampo = ( JTextFieldPad ) comp;
			iTipo = txtCampo.getTipo();
		}
		else if (( comp instanceof JPasswordFieldPad ) || ( comp instanceof JTextAreaPad )) {
			iTipo = JTextFieldPad.TP_STRING;
		}
		else if (comp instanceof JCheckBoxPad) {
			iTipo = ( ( JCheckBoxPad ) comp ).getTipo();
		}
		else if (comp instanceof JComboBoxPad) {
			iTipo = ( ( JComboBoxPad ) comp ).getTipo();
		}
		else if (comp instanceof JRadioGroup<?, ?>) {
			iTipo = ( ( JRadioGroup<?, ?> ) comp ).getTipo();
		}
		else if (comp instanceof PainelImagem) {
			iTipo = PainelImagem.TP_BYTES;
		}
		if (descFK == null)
			txtDescFK = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);
		else
			txtDescFK = descFK;
		setRequerido(req);
	}

	public void setGuardaCampo(Component c, String nome, String label, byte key, JTextFieldFK descFK, boolean req) {
		sNome = nome;
		sLabel = label;
		comp = c;
		bRequerido = req;
		bPK = ( key == ListaCampos.DB_PK ) || ( key == ListaCampos.DB_PF );
		bFK = ( key == ListaCampos.DB_FK ) || ( key == ListaCampos.DB_PF );
		if (comp instanceof JTextFieldPad) {
			txtCampo = ( JTextFieldPad ) comp;
			// txtCampo.setPKFK(bPK,bFK);
			iTipo = txtCampo.getTipo();
		}
		else if (( comp instanceof JPasswordFieldPad ) || ( comp instanceof JTextAreaPad )) {
			iTipo = JTextFieldPad.TP_STRING;
		}
		else if (comp instanceof JCheckBoxPad) {
			iTipo = ( ( JCheckBoxPad ) comp ).getTipo();
		}
		else if (comp instanceof JComboBoxPad) {
			iTipo = ( ( JComboBoxPad ) comp ).getTipo();
		}
		else if (comp instanceof JRadioGroup<?, ?>) {
			iTipo = ( ( JRadioGroup<?, ?> ) comp ).getTipo();
		}
		else if (comp instanceof PainelImagem) {
			iTipo = PainelImagem.TP_BYTES;
		}
		if (descFK == null)
			txtDescFK = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);
		else
			txtDescFK = descFK;
		setRequerido(req);
	}

	public String getLabel() {
		return sLabel;
	}

	public JTextFieldPad getDescFK() {
		JTextFieldPad txtRet = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);
		if (txtDescFK == null) {
			for (int i = 0; i < txtCampo.getTabelaExterna().getComponentCount(); i++)
				if (!( ( GuardaCampo ) txtCampo.getTabelaExterna().getComponent(i) ).bPK && ( ( GuardaCampo ) txtCampo.getTabelaExterna().getComponent(i) ).getCampo() != null) {
					txtRet = (JTextFieldPad) ( ( GuardaCampo ) txtCampo.getTabelaExterna().getComponent(i) ).getCampo();
				}
		}
		else
			txtRet = txtDescFK;
		return txtRet;
	}

	public Campo getCampo() {
		if (comp instanceof JTextAreaPad) {
			return (JTextAreaPad) comp;
		}
		if (comp instanceof JComboBoxPad) {
			return (JComboBoxPad) comp;
		}

		return txtCampo;
	}

	public Component getComponente() {
		return comp;
	}

	public void atualizaFK() {
		if (( bFK ) & ( comp instanceof JTextFieldPad )) {
			( ( JTextFieldPad ) comp ).atualizaFK();
		}
	}

	public String strFormat(String sVal) {
		String sRet = sVal;
		if (comp instanceof JTextFieldPad) {
			sRet = Funcoes.setMascara(sVal, ( ( JTextFieldPad ) comp ).getStrMascara());
		}
		return sRet;
	}

	public String getNomeCampo() {
		if (sNome!=null) {
			return sNome.toLowerCase();
		}
		return sNome;
	}

	public boolean ehVisivel() {
		return bVisivel;
	}

	public int getTipo() {
		return iTipo;
	}

	public String getTituloCampo() {
		return sLabel;
	}

	public int getTamanhoCampo() {
		return comp.getSize().width;
	}

	public boolean getSoLeitura() {
		boolean bRetorno = false;
		if (comp instanceof JTextFieldPad) {
			bRetorno = ( ( JTextFieldPad ) comp ).getSoLeitura();
		}
		return bRetorno;
	}

	public boolean ehNulo() {
		if (comp instanceof JTextFieldPad) {
			if (( ( JTextFieldPad ) comp ).getText().trim().length() == 0) {
				return true;
			}
		}
		else if (comp instanceof JTextAreaPad) {
			if (( ( JTextAreaPad ) comp ).getText().trim().length() == 0)
				return true;
		}
		else if (comp instanceof PainelImagem) {
			return ( ( ( PainelImagem ) comp ).ehNulo() );
		}
		return false;
	}

	private void setRequerido(boolean bReq) {
		bRequerido = bReq;
		if (bRequerido) {
			if (comp instanceof JTextFieldPad) {
				( ( JTextFieldPad ) comp ).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
				( ( JTextFieldPad ) comp ).setRequerido(bReq);
			}
			else if (comp instanceof JRadioGroup<?, ?>)
				( ( JRadioGroup<?, ?> ) comp ).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
			else if (comp instanceof JTextAreaPad)
				( ( JTextAreaPad ) comp ).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
			else if (comp instanceof JCheckBoxPad)
				( ( JCheckBoxPad ) comp ).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
			else if (comp instanceof JComboBoxPad)
				( ( JComboBoxPad ) comp ).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
			else if (comp instanceof PainelImagem)
				( ( PainelImagem ) comp ).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createEtchedBorder()));
		}
	}

	public boolean getRequerido() {
		return bRequerido;
	}

	public boolean ehPK() {
		return bPK;
	}

	public boolean ehFK() {
		return bFK;
	}

	public void limpa() throws ExceptionLimpaComponent {
		byte[] bVals = new byte[1];

		if (( bFK ) && ( txtCampo != null )) {
			try {
				txtCampo.getTabelaExterna().limpaCampos(false);
			}
			catch (Exception e) {
				throw new ExceptionLimpaComponent("Tabela externa nula ao limpar o componente do campo " + txtCampo.getNomeCampo());
			}
		}

		if (comp instanceof JTextFieldPad)
			( ( JTextFieldPad ) comp ).setVlrString("");
		else if (comp instanceof JTextAreaPad)
			( ( JTextAreaPad ) comp ).setVlrString("");
		else if (comp instanceof JTextFieldFK)
			( ( JTextFieldFK ) comp ).setVlrString("");
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setSelected(false);
		else if (comp instanceof JComboBoxPad)
			( ( JComboBoxPad ) comp ).limpa();
		else if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).novo();
		else if (comp instanceof JPasswordFieldPad)
			( ( JPasswordFieldPad ) comp ).setVlrString("");
		else if (comp instanceof PainelImagem) {
			( ( PainelImagem ) comp ).setVlrBytes(bVals);
			( ( PainelImagem ) comp ).repaint();
		}
		// System.out.println("Campo: "+sNome);

	}

	public String getVlr() {
		String sRetorno = "";
		if (comp instanceof JTextFieldPad)
			sRetorno = ( ( JTextFieldPad ) comp ).getText();
		else if (comp instanceof JTextAreaPad)
			sRetorno = ( ( JTextAreaPad ) comp ).getText();
		else if (comp instanceof JCheckBoxPad)
			sRetorno = ( ( JCheckBoxPad ) comp ).getVlrString();
		else if (comp instanceof JRadioGroup<?, ?>)
			sRetorno = ( ( JRadioGroup<?, ?> ) comp ).getVlrString();
		else if (comp instanceof JComboBoxPad) {
			sRetorno = ( ( JComboBoxPad ) comp ).getVlrString();
		}
		else if (comp instanceof JPasswordFieldPad)
			sRetorno = ( ( JPasswordFieldPad ) comp ).getVlrString();
		return sRetorno;
	}

	public String getVlrString() {
		String sRetorno = "";
		if (comp instanceof JTextFieldPad)
			sRetorno = ( ( JTextFieldPad ) comp ).getVlrString();
		else if (comp instanceof JTextAreaPad)
			sRetorno = ( ( JTextAreaPad ) comp ).getVlrString();
		else if (comp instanceof JCheckBoxPad)
			sRetorno = ( ( JCheckBoxPad ) comp ).getVlrString();
		else if (comp instanceof JRadioGroup<?, ?>)
			sRetorno = ( ( JRadioGroup<?, ?> ) comp ).getVlrString();
		else if (comp instanceof JComboBoxPad)
			sRetorno = ( ( JComboBoxPad ) comp ).getVlrString();
		else if (comp instanceof JPasswordFieldPad)
			sRetorno = ( ( JPasswordFieldPad ) comp ).getVlrString();
		return sRetorno;
	}

	public Integer getVlrInteger() {
		Integer iRetorno = new Integer(0);
		if (comp instanceof JTextFieldPad)
			iRetorno = ( ( JTextFieldPad ) comp ).getVlrInteger();
		else if (comp instanceof JCheckBoxPad)
			iRetorno = ( ( JCheckBoxPad ) comp ).getVlrInteger();
		else if (comp instanceof JComboBoxPad)
			iRetorno = ( ( JComboBoxPad ) comp ).getVlrInteger();
		else if (comp instanceof JRadioGroup<?, ?>)
			iRetorno = ( ( JRadioGroup<?, ?> ) comp ).getVlrInteger();
		return iRetorno;
	}

	public BigDecimal getVlrBigDecimal() {
		BigDecimal bigRetorno = new BigDecimal(0);
		if (comp instanceof JTextFieldPad)
			bigRetorno = ( ( JTextFieldPad ) comp ).getVlrBigDecimal();
		return bigRetorno;
	}

	public Double getVlrDouble() {
		Double dRetorno = new Double(0);
		if (comp instanceof JTextFieldPad)
			dRetorno = ( ( JTextFieldPad ) comp ).getVlrDouble();
		return dRetorno;
	}

	public Date getVlrTime() {
		Date dRetorno = new Date();
		if (comp instanceof JTextFieldPad)
			dRetorno = ( ( JTextFieldPad ) comp ).getVlrTime();
		return dRetorno;
	}

	public Date getVlrDate() {
		Date dRetorno = new Date();
		if (comp instanceof JTextFieldPad)
			dRetorno = ( ( JTextFieldPad ) comp ).getVlrDate();
		return dRetorno;
	}

	public DadosImagem getVlrBytes() {
		DadosImagem diRetorno = null;
		if (comp instanceof PainelImagem)
			diRetorno = ( ( PainelImagem ) comp ).getVlrBytes();
		return diRetorno;
	}

/*	public void setVlrString(String val) {
		this.setVlrString(val, true, this);
	}*/

	public void setVlrString(String val) {
		if (val == null)
			val = "";
		if (comp instanceof JTextFieldPad)
			( ( JTextFieldPad ) comp ).setVlrString(val);
		else if (comp instanceof JTextAreaPad)
			( ( JTextAreaPad ) comp ).setVlrString(val);
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setVlrString(val);
		else if (comp instanceof JComboBoxPad) {
			( ( JComboBoxPad ) comp ).setVlrString(val.trim());
		}
		else if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).setVlrString(val);
		else if (comp instanceof JPasswordFieldPad)
			( ( JPasswordFieldPad ) comp ).setVlrString(val);
	}

	//public void setVlrInteger(Integer val) {
		//this.setVlrInteger(val, true, this);
	//}
	
	public void setVlrInteger(Integer val) {
		if (comp instanceof JTextFieldPad)
			( ( JTextFieldPad ) comp ).setVlrInteger(val);
		else if (comp instanceof JCheckBoxPad)
			( ( JCheckBoxPad ) comp ).setVlrInteger(val);
		else if (comp instanceof JComboBoxPad) {
			//( ( JComboBoxPad ) comp ).setCarregando(carregando);
			( ( JComboBoxPad ) comp ).setVlrInteger(val);
		}
		else if (comp instanceof JRadioGroup<?, ?>)
			( ( JRadioGroup<?, ?> ) comp ).setVlrInteger(val);
	}

	public void setVlrBigDecimal(BigDecimal val) {
		if (val == null)
			( ( JTextFieldPad ) comp ).setText("");
		else if (comp instanceof JTextFieldPad)
			( ( JTextFieldPad ) comp ).setVlrBigDecimal(val);
	}
/*
	public void setVlrDouble(Double val) {
		if (val == null)
			( ( JTextFieldPad ) comp ).setText("");
		else if (comp instanceof JTextFieldPad)
			( ( JTextFieldPad ) comp ).setVlrDouble(val);
	}
*/
	public void setVlrBytes(byte[] bVals) {
		if (bVals == null)
			( ( PainelImagem ) comp ).setVlrBytes(new byte[1]);
		else if (comp instanceof PainelImagem)
			( ( PainelImagem ) comp ).setVlrBytes(bVals);
	}

	public void setVlrBytes(InputStream bVals) {
		if (comp instanceof PainelImagem) {
			( ( PainelImagem ) comp ).setVlrBytes(bVals);

		}
	}

}
