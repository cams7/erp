/**
 * @version 07/2007 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLChecaExporta.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                         Dialogo que mostra os registros com erros da exportação Contabil/Livros Fiscais.
 * 
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.componet.integration.Contabil;
import org.freedom.library.business.componet.integration.SafeContabil.SafeContabilVO;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.DLRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class DLChecaExporta extends DLRelatorio {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JInternalFrame orig;

	private DbConnection con = null;

	public JTablePad tab = new JTablePad();

	private final String tipo;

	private final String sistema;

	public DLChecaExporta( final JInternalFrame orig, final String tipo, final String sistema ) {

		super();
		setModal( true );
		setTitulo( "Inconsistências de Vendas" );
		setAtribos( 600, 400 );

		this.orig = orig;
		this.tipo = tipo;
		this.sistema = sistema;

		c.add( panelGeral, BorderLayout.CENTER );

		montaTela();
	}

	private void montaTela() {

		if ( Contabil.SAFE_CONTABIL.equals( sistema ) ) {
			montaTelaSafeContabil();
		}
	}

	private void montaTelaSafeContabil() {

		panelGeral.add( new JScrollPane( tab ), BorderLayout.CENTER );

		tab.adicColuna( "Tipo" );
		tab.adicColuna( "Conta Deb." );
		tab.adicColuna( "Conta Cred." );
		tab.adicColuna( "Documento" );
		tab.adicColuna( "Data" );
		tab.adicColuna( "Valor" );
		tab.adicColuna( "Historico" );
		tab.adicColuna( "Filial" );

		tab.setTamColuna( 100, 0 );
		tab.setTamColuna( 100, 1 );
		tab.setTamColuna( 100, 2 );
		tab.setTamColuna( 80, 3 );
		tab.setTamColuna( 90, 4 );
		tab.setTamColuna( 100, 5 );
		tab.setTamColuna( 300, 6 );
		tab.setTamColuna( 50, 7 );
	}

	@ SuppressWarnings ( "unchecked" )
	public void carregaDados( List<?> dados ) {

		if ( Contabil.SAFE_CONTABIL.equals( sistema ) ) {
			try {
				carregaDadosSafeContabil( (List<SafeContabilVO>) dados );
			} catch ( ClassCastException e ) {
				e.printStackTrace();
			}
		}

	}

	private void carregaDadosSafeContabil( List<SafeContabilVO> args ) {

		tab.limpa();
		int row = 0;

		for ( SafeContabilVO sb : args ) {

			tab.adicLinha();

			tab.setValor( sb.getTipo().getDescricao(), row, 0 );
			tab.setValor( sb.getContadeb(), row, 1 );
			tab.setValor( sb.getContacred(), row, 2 );
			tab.setValor( sb.getDocumento(), row, 3 );
			tab.setValor( sb.getData(), row, 4 );
			tab.setValor( sb.getValor().setScale( 2, BigDecimal.ROUND_HALF_UP ), row, 5 );
			tab.setValor( sb.getHistorico(), row, 6 );
			tab.setValor( sb.getFilial(), row, 7 );

			row++;
		}

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		try {

			String linha = "|" + StringFunctions.replicate( "-", 133 ) + "|";
			String tipo = "";
			ImprimeOS imp = new ImprimeOS( "", con );
			int linPag = imp.verifLinPag() - 1;

			imp.montaCab();
			imp.setTitulo( "Relatório de Classificação de Clientes" );

			imp.limpaPags();

			for ( int i = 0; i < tab.getNumLinhas(); i++ ) {

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, false );

					imp.say( 0, "|" );
					imp.say( 1, "Doc." );
					imp.say( 10, "|" );
					imp.say( 11, "Conta Cred." );
					imp.say( 22, "|" );
					imp.say( 23, "Conta Deb." );
					imp.say( 34, "|" );
					imp.say( 35, "Data" );
					imp.say( 46, "|" );
					imp.say( 47, "Valor" );
					imp.say( 60, "|" );
					imp.say( 61, "Historico" );
					imp.say( 135, "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, linha );
				}

				if ( !tipo.equals( (String) tab.getValor( i, 0 ) ) ) {

					tipo = (String) tab.getValor( i, 0 );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, linha );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( ( ( 133 - tipo.length() ) / 2 - 1 ), tipo );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, linha );
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 1, (String) tab.getValor( i, 3 ) );
				imp.say( 10, "|" );
				imp.say( 14, (String) tab.getValor( i, 2 ) );
				imp.say( 22, "|" );
				imp.say( 23, (String) tab.getValor( i, 1 ) );
				imp.say( 34, "|" );
				imp.say( 35, Funcoes.dateToStrDate( (Date) tab.getValor( i, 4 ) ) );
				imp.say( 46, "|" );
				imp.say( 47, Funcoes.strDecimalToStrCurrency( 12, 2, ( (BigDecimal) tab.getValor( i, 5 ) ).toString() ) );
				imp.say( 60, "|" );
				imp.say( 61, Funcoes.copy( (String) tab.getValor( i, 6 ), 73 ) );
				imp.say( 135, "|" );

				if ( imp.pRow() >= linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow() + 0, 0, linha );
					imp.incPags();
					imp.eject();
				}
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, linha );
			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( orig );
			}
			else {
				imp.print();
			}

			dispose();

		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, con, err );
		}
	}

	public void setConexao( final DbConnection con ) {

		this.con = con;
	}

};
