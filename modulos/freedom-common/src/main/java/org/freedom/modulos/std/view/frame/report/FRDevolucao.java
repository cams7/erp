/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRCompras.java <BR>
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
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRDevolucao extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcFor = new ListaCampos( this );

	private ListaCampos lcPlanoPag = new ListaCampos( this );

	private JTextFieldPad txtIdUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	public FRDevolucao() {

		setTitulo( "Relatório de devolução" );
		setAtribos( 50, 50, 345, 240 );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.plano.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		txtCodPlanoPag.setFK( true );
		lcPlanoPag.setReadOnly( true );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 17, 0, 80, 20 );
		adic( lbLinha, 7, 10, 300, 40 );
		adic( new JLabelPad( "De:" ), 13, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 152, 25, 37, 20 );
		adic( txtDatafim, 190, 25, 100, 20 );
		adic( new JLabelPad( "Cód.pl.pag." ), 7, 55, 80, 20 );
		adic( txtCodPlanoPag, 7, 75, 80, 20 );
		adic( new JLabelPad( "Descrição do plano de pagamento" ), 90, 55, 200, 20 );
		adic( txtDescPlanoPag, 90, 75, 215, 20 );
		adic( new JLabelPad( "Usuário" ), 7, 100, 100, 20 );
		adic( txtIdUsu, 7, 120, 100, 20 );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFor.setConexao( cn );
		lcPlanoPag.setConexao( cn );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;

		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sWhere = "";
		BigDecimal bTotal = null;
		ImprimeOS imp = null;
		int linPag = 0;
		int iparam = 1;
		int iCab = 7;
		int iCodCompra = 0;
		String idusu = txtIdUsu.getVlrString().trim().toUpperCase();

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Devolução" );
			imp.addSubTitulo( "RELATORIO DE DEVOLUÇÂO" );
			imp.addSubTitulo( "PERIODO DE: " + txtDataini.getVlrString() + " Até: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			bTotal = new BigDecimal( "0" );

			if ( txtCodPlanoPag.getVlrInteger().intValue() > 0 ) {
				sWhere += " AND C.CODPLANOPAG = " + txtCodPlanoPag.getVlrInteger().intValue();
				imp.addSubTitulo( "PLANO DE PAGAMENTO: " + txtDescPlanoPag.getVlrString() );
				iCab++;
			}
			if ( !"".equals( idusu ) ) {
				sWhere += " AND C.IDUSUINS=? ";
			}

			sSQL = "SELECT C.CODCOMPRA, C.DOCCOMPRA, C.DTEMITCOMPRA, C.DTENTCOMPRA, C.VLRLIQCOMPRA, " + "F.NOMEFOR, PG.DESCPLANOPAG, " + "IT.CODITCOMPRA, IT.CODPROD, PD.DESCPROD, IT.CODLOTE, IT.QTDITCOMPRA, " + "IT.VLRLIQITCOMPRA, IT.PERCDESCITCOMPRA, IT.VLRDESCITCOMPRA, IT.VLRLIQITCOMPRA "
					+ "FROM CPCOMPRA C, CPITCOMPRA IT, CPFORNECED F, FNPLANOPAG PG, EQPRODUTO PD, EQTIPOMOV TM " + "WHERE C.CODEMP=? AND C.CODFILIAL=? " + "AND C.CODEMPTM=TM.CODEMP AND C.CODFILIALTM=TM.CODFILIAL AND C.CODTIPOMOV=TM.CODTIPOMOV AND TM.TIPOMOV='DV' "
					+ "AND C.CODEMPFR=F.CODEMP AND C.CODFILIALFR=F.CODFILIAL AND C.CODFOR=F.CODFOR " + "AND C.CODEMPPG=PG.CODEMP AND C.CODFILIALPG=PG.CODFILIAL AND C.CODPLANOPAG=PG.CODPLANOPAG " + "AND C.CODEMP=IT.CODEMP AND C.CODFILIAL=IT.CODFILIAL AND C.CODCOMPRA=IT.CODCOMPRA "
					+ "AND IT.CODEMPPD=PD.CODEMP AND IT.CODFILIALPD=PD.CODFILIAL AND IT.CODPROD=PD.CODPROD " + "AND C.DTEMITCOMPRA BETWEEN ? AND ? " + sWhere + " ORDER BY C.CODCOMPRA, IT.CODITCOMPRA";

			ps = con.prepareStatement( sSQL );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDataini.getVlrString() ) );
			ps.setDate( iparam++, Funcoes.strDateToSqlDate( txtDatafim.getVlrString() ) );

			if ( !"".equals( idusu ) ) {
				ps.setString( iparam++, idusu );
			}

			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.eject();
					imp.incPags();

				}
				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );

				}

				if ( iCodCompra != rs.getInt( "CODCOMPRA" ) ) {

					iCodCompra = rs.getInt( "CODCOMPRA" );

					if ( imp.pRow() > iCab ) {

						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
					}
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| N. Compra" );
					imp.say( imp.pRow(), 13, "| Doc" );
					imp.say( imp.pRow(), 22, "| Fornecedor" );
					imp.say( imp.pRow(), 58, "| Plano de Pagamento" );
					imp.say( imp.pRow(), 94, "| Valor" );
					imp.say( imp.pRow(), 109, "|  Emissao" );
					imp.say( imp.pRow(), 122, "|  Entrada" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| " + ( rs.getString( "CODCOMPRA" ) != null ? rs.getString( "CODCOMPRA" ) : "" ) );
					imp.say( imp.pRow(), 13, "| " + ( rs.getString( "DOCCOMPRA" ) != null ? rs.getString( "DOCCOMPRA" ) : "" ) );
					imp.say( imp.pRow(), 22, "| " + ( rs.getString( "NOMEFOR" ) != null ? ( rs.getString( "NOMEFOR" ).length() > 34 ? rs.getString( "NOMEFOR" ).substring( 0, 34 ) : rs.getString( "NOMEFOR" ) ) : "" ) );
					imp.say( imp.pRow(), 58, "| " + ( rs.getString( "DESCPLANOPAG" ) != null ? ( rs.getString( "DESCPLANOPAG" ).length() > 34 ? rs.getString( "DESCPLANOPAG" ).substring( 0, 34 ) : rs.getString( "DESCPLANOPAG" ) ) : "" ) );
					imp.say( imp.pRow(), 94, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, ( rs.getString( "VLRLIQCOMPRA" ) != null ? rs.getString( "VLRLIQCOMPRA" ) : "" ) ) );
					imp.say( imp.pRow(), 109, "| " + Funcoes.dateToStrDate( rs.getDate( "DTEMITCOMPRA" ) ) );
					imp.say( imp.pRow(), 122, "| " + Funcoes.dateToStrDate( rs.getDate( "DTENTCOMPRA" ) ) );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| Item" );
					imp.say( imp.pRow(), 8, "| Cod.prod." );
					imp.say( imp.pRow(), 22, "| Descricao do produto" );
					imp.say( imp.pRow(), 75, "| Lote" );
					imp.say( imp.pRow(), 91, "| Qtd" );
					imp.say( imp.pRow(), 102, "| Vlr.Item" );
					imp.say( imp.pRow(), 115, "|% Desc" );
					imp.say( imp.pRow(), 122, "|  Vlr.desc" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "| " + ( rs.getString( "CODITCOMPRA" ) != null ? rs.getString( "CODITCOMPRA" ) : "" ) );
				imp.say( imp.pRow(), 8, "| " + ( rs.getString( "CODPROD" ) != null ? rs.getString( "CODPROD" ) : "" ) );
				imp.say( imp.pRow(), 22, "| " + ( rs.getString( "DESCPROD" ) != null ? rs.getString( "DESCPROD" ) : "" ) );
				imp.say( imp.pRow(), 75, "| " + ( rs.getString( "CODLOTE" ) != null ? rs.getString( "CODLOTE" ) : "" ) );
				imp.say( imp.pRow(), 91, "| " + Funcoes.strDecimalToStrCurrency( 8, Aplicativo.casasDec, ( rs.getString( "QTDITCOMPRA" ) != null ? rs.getString( "QTDITCOMPRA" ) : "" ) ) );
				imp.say( imp.pRow(), 102, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, ( rs.getString( "VLRLIQITCOMPRA" ) != null ? rs.getString( "VLRLIQITCOMPRA" ) : "" ) ) );
				imp.say( imp.pRow(), 115, "| " + Funcoes.strDecimalToStrCurrency( 5, 2, ( rs.getString( "PERCDESCITCOMPRA" ) != null ? rs.getString( "PERCDESCITCOMPRA" ) : "" ) ) );
				imp.say( imp.pRow(), 122, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, ( rs.getString( "VLRDESCITCOMPRA" ) != null ? rs.getString( "VLRDESCITCOMPRA" ) : "" ) ) );
				imp.say( imp.pRow(), 135, "|" );
				bTotal = bTotal.add( rs.getBigDecimal( "VLRLIQITCOMPRA" ) );
			}

			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "| " );
			imp.say( imp.pRow(), 94, "VALOR TOTAL DE DEVOLUÇÂO = " + Funcoes.strDecimalToStrCurrency( 13, 2, bTotal.toString() ) );
			imp.say( imp.pRow(), 135, "|" );
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

			imp.eject();
			imp.fechaGravacao();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela devolução!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} catch ( Exception err ) {
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			sWhere = null;
			bTotal = null;
			System.gc();
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}
}
