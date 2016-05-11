/**
 * @version 08/11/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm.view.dialog.utility; <BR>
 *         Classe: @(#)DLModeloAtend.java <BR>
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
 *         Tela que retorna um modelo de atendimento.
 * 
 */

package org.freedom.modulos.crm.view.dialog.utility;

import java.awt.Component;
import java.sql.SQLException;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.business.object.Atendimento;
import org.freedom.modulos.crm.dao.DAOAtendimento;
import org.freedom.modulos.crm.view.frame.crud.plain.FModAtendo;
import org.freedom.modulos.crm.view.frame.crud.plain.FNovoAtend;

public class DLModeloAtend extends FFDialogo {
	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodmodel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldFK txtDescmodel = new JTextFieldFK( JTextFieldFK.TP_STRING, 100, 0 );
	
	private Component corig = null;
	
	private DAOAtendimento daoatend = null;
	
	private FNovoAtend dl = null;
	
	private Atendimento atd = null;
	
	private ListaCampos lcModAtendo = new ListaCampos( this );


	public DLModeloAtend() {
		super();
		setTitulo( "Modelo de atendimento" );
		setAtribos( 50, 50, 450, 200 );
		montaListaCampos();
		montaTela();
		
	}
	
	private void montaTela(){
		
		adic( txtCodmodel, 7, 30, 80, 20, "Cód.model." );
		adic( txtDescmodel, 90, 30, 300, 20, "Descrição do modelo" );
		
	}
	
	private void montaListaCampos(){
		
		lcModAtendo.add( new GuardaCampo( txtCodmodel, "CodModel", "Cód.Model", ListaCampos.DB_PK, false ) );
		lcModAtendo.add( new GuardaCampo( txtDescmodel, "DescModel", "Descrição do atendimento", ListaCampos.DB_SI, false ) );
		lcModAtendo.montaSql( false, "MODATENDO", "AT" );
		lcModAtendo.setReadOnly( true );
		txtCodmodel.setNomeCampo( "CodModel" );
		txtCodmodel.setFK( true );
		txtCodmodel.setTabelaExterna( lcModAtendo, FModAtendo.class.getCanonicalName() );
		
	}
	
	public Integer getCodModel(){
		Integer result = null;
		if(!"".equals( txtCodmodel.getText().trim() ) ) {
			result = txtCodmodel.getVlrInteger();
		}
		return result;
	}
	
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcModAtendo.setConexao( cn );
		
		daoatend = new DAOAtendimento( cn );
		try {
			daoatend.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}
		

	}
	
}
