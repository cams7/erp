/**
 * @version 19/09/2011 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)DLRegBatida.java <BR>
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
 *         Tela que registra Batida de ponto do empregado.
 * 
 */

package org.freedom.modulos.crm.view.dialog.utility;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.business.object.Atendimento.PREFS;
import org.freedom.modulos.crm.dao.DAOAtendimento;
import org.freedom.modulos.gpe.business.object.Batida;
import org.freedom.modulos.gpe.dao.DAOBatida;
import org.freedom.modulos.grh.view.frame.crud.plain.FTurnos;

public class DLRegBatida extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextFieldFK txtMatempr = new JTextFieldFK( JTextFieldFK.TP_INTEGER, 6, 0 );
	
	private final JTextFieldFK txtNomeempr = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0);
	
	private final JTextFieldFK txtDtbat = new JTextFieldFK( JTextFieldFK.TP_DATE, 10, 0 );

	private final JTextFieldPad txtHbat = new JTextFieldPad( JTextFieldPad.TP_TIME, 5, 0 );
	
	private final ListaCampos lcBatida = new ListaCampos(this);
	
	private final ListaCampos lcEmpr = new ListaCampos(this, "EP");
	
	private Batida batida = null;
	
	private DAOBatida daobatida = null;
	
	public DLRegBatida() {

		super();
		setTitulo( "Registro da batida." );
		setAtribos( 50, 50, 450, 200 );
		montaListaCampos();
		montaTela();
	
	}
	
	public void montaTela(){

		adic( txtMatempr, 7, 25, 70, 20, "Matrícula" );
		adic( txtNomeempr, 80, 25, 300, 20, "Nome");
		adic( txtDtbat, 7, 65, 90, 20, "Data" );
		adic( txtHbat, 100, 65, 90, 20, "Horário");
		
	}
	
	public void setValores(DAOBatida daobatida, Batida batida) {
		this.daobatida = daobatida;
		this.batida = batida;
		txtMatempr.setVlrInteger( batida.getMatempr() );
		lcEmpr.carregaDados();
		txtDtbat.setVlrDate( batida.getDataponto() );
		txtHbat.setVlrString( batida.getHoraponto() );
	}
	
	public void montaListaCampos(){
		lcEmpr.add( new GuardaCampo( txtMatempr, "Matempr", "Matrícula", ListaCampos.DB_PK, false ) );
		lcEmpr.add( new GuardaCampo( txtNomeempr, "Nomeempr", "Nome", ListaCampos.DB_SI, false ) );
		lcEmpr.montaSql( false, "EMPREGADO", "RH" );
		lcEmpr.setQueryCommit( false );
		lcEmpr.setReadOnly( true );
		txtMatempr.setTabelaExterna( lcEmpr, FTurnos.class.getCanonicalName() );
	}

	public void actionPerformed( ActionEvent evt ) {
		boolean result = false;
		if (evt.getSource()==btOK) {
            result = gravaBatida();			
		} else if (evt.getSource()==btCancel) {
			result = true;
		}
		if ( result ) {
			super.actionPerformed( evt );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcEmpr.setConexao( cn );

	}
	
	public boolean gravaBatida() {
		boolean result = true;
		String horaant = null;
		if (daobatida!=null) {
			try {
				horaant = batida.getHoraponto();
				batida.setCodemp( Aplicativo.iCodEmp );
				batida.setCodfilial( ListaCampos.getMasterFilial( "PEBATIDA" ) );
				batida.setHoraponto( txtHbat.getVlrString() );
				daobatida.executeProcInsereBatida( batida );
				if ( "A".equals( batida.getAftela() ) ) {
					insertIntervaloChegada(txtHbat.getVlrString(), horaant);
				} else {
					insertIntervaloChegada(horaant, txtHbat.getVlrString());
				}
			} catch (SQLException e) {
				result = false;
				Funcoes.mensagemErro( this, "Erro registrando o ponto !\n" + e.getMessage());
			}
		}
		return result;
	}
	
	private void insertIntervaloChegada(String horaini, String horafim) {
		
		Long diferenca = null;
		String horaPrimUltLancto = null;
		boolean gravaLanca = false;
		horaini=Funcoes.copy( horaini, 5 );
		horafim=Funcoes.copy( horafim, 5 );
		
		diferenca = Funcoes.subtraiTime( Funcoes.strTimeTosqlTime( horaini), Funcoes.strTimeTosqlTime( horafim ) );
		
		if ( diferenca>0 ) {
			// Cria um DAOAtendimento para inserção de atendimento
			DAOAtendimento daoatend = new DAOAtendimento( con );
			try {
				// Carrega o preferências com modelo de atendimento para intervalo de chegada ou saída.
				daoatend.setPrefs( Aplicativo.iCodEmp , ListaCampos.getMasterFilial("SGPREFERE3") );
				// Continua se tiver modelo de atendimento para intervalo de chegada ou saída
				if ( daoatend.getPrefs()[PREFS.CODMODELME.ordinal()] != null) {
					// Verifica se já existe lançamento para o atendente e retorna a hora do atendimento.
					horaPrimUltLancto = daoatend.getHoraPrimUltLanca( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ),
							batida.getDataponto(), horaini, horafim,
							batida.getCodempae(), batida.getCodfilialae(), batida.getCodatend(),
							batida.getAftela() );
					if ("A".equals( batida.getAftela() )) {
						// Caso não exista lançamentos no CRM ou o horário do primeiro atendimento seja maior que a hora final do atendimento continua.
						if ( (horaPrimUltLancto==null) || (horafim.compareTo( horaPrimUltLancto )<0) ) {
							// Se tiver atendimento o horário final do intervalo deverá ser o horário inicial do atendimento já registrado. 
							if (horaPrimUltLancto!=null) {
								horafim = horaPrimUltLancto;
							}
							gravaLanca = true;
						}
					} else {
						// Caso exista lançamentos no CRM antes do fechamento do turno e a horaini seja maior que a hora final do último lançamento.
						if ( (horaPrimUltLancto!=null) ) {
							if (horaini.compareTo( horaPrimUltLancto )>0) {
								horaini = horaPrimUltLancto;
							}
							gravaLanca = true;
						}
					}
					if (gravaLanca) {
						daoatend.insertIntervaloChegada( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "ATATENDIMENTO" ), 
							  batida.getDataponto(), batida.getDataponto(), horaini, horafim, 
							  batida.getCodempae(), batida.getCodfilialae(), batida.getCodatend(), 
							  batida.getCodempus(), batida.getCodfilialus(), batida.getIdusu() );
					}

				}
			}
			catch (Exception e) {
				Funcoes.mensagemErro( this, "Erro inserindo lançamento automatizado de intervalo !\n" + e.getMessage() );
				e.printStackTrace();
			}			
		}
	}
}
