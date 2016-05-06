/**
 * @version 27/04/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FQualificacao.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Tela de cadastro de qualificações de chamados.
 * 
 */

package org.freedom.modulos.fnc.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.dialog.report.DLRSinalizadores;
import org.freedom.modulos.std.view.frame.crud.special.FLanca;

public class FSinalizadores extends FDados implements ActionListener, ChangeListener, PostListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodSinal= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescSinal = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtCorSinal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JColorChooser tcc = new JColorChooser();
	
	private JPanelPad pinCor = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
	
	private Object owner = null; 
		
	public FSinalizadores() {

		super();

		nav.setNavigation( true );

		setTitulo( "Sinalizadores" );
		
		setAtribos( 50, 50, 530, 480 );
		
		adicCampo( txtCodSinal, 7, 20, 100, 20, "CODSINAL", "Cod.Sinal.", ListaCampos.DB_PK, true );		
		adicCampo( txtDescSinal, 110, 20, 395, 20, "DESCSINAL", "Descrição do sinalizador", ListaCampos.DB_SI, true );
		adicCampoInvisivel( txtCorSinal, "CORSINAL", "", ListaCampos.DB_SI, false );
		adic( pinCor, 7 , 50, 500, 350 );
	
		
		setListaCampos( true, "SINAL", "FN" );
		

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
			
		pinCor.add( tcc, BorderLayout.CENTER );
		tcc.getSelectionModel().addChangeListener( this );
		
		setImprimir( true );
		
		lcCampos.addPostListener( this );
		
	}

	public void setOwner(Object obj) {
		owner = obj;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLRSinalizadores sl = null;
		ImprimeOS imp = null;
		int linPag = 0;

		sl = new DLRSinalizadores();
		sl.setVisible( true );
		if ( sl.OK == false ) {
			sl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Sinalizadores" );
			imp.limpaPags();

			sSQL = "SELECT CODSINAL,DESCSINAL FROM FNSINAL WHERE CODEMP=? AND CODFILIAL=? ORDER BY " + sl.getValor();

			ps = con.prepareStatement( sSQL );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNSINAL" ) );
			
			rs = ps.executeQuery();
			
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Cód.Sinal.." );
					imp.say( imp.pRow(), 30, "Descrição de Sinalizadores" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodSinal" ) );
				imp.say( imp.pRow(), 30, rs.getString( "DescSinal" ) );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();
			con.commit();
			sl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de Sinalizadores!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			sl = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	public void afterCarrega( CarregaEvent pevt ) {
		
		Color cor = new Color( txtCorSinal.getVlrInteger() );

		tcc.setColor( cor );
		tcc.repaint();
		tcc.revalidate();
	}
	public void stateChanged( ChangeEvent e ) {

		Color cor = tcc.getColor();
		txtCorSinal.setVlrInteger( cor.getRGB() );
	}
	
	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {
			if (owner!=null) {
				if (owner instanceof FLanca) {
					((FLanca) owner).montaMenuCores( false );
				}
			}
		}
	}

	public void beforePost( PostEvent pevt ) {

	}

}
