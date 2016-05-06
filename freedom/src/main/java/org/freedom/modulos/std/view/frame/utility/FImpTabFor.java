/**
 * @version 25/02/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FGeraFiscal.java <BR>
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

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;

public class FImpTabFor extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 205 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtNomeArq = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JButtonPad btBuscaArq = new JButtonPad( Icone.novo( "btAbrirPeq.png" ) );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JProgressBar pbAnd = new JProgressBar();

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private int iAnd = 0;

	private int iLinha = 0;

	private int iTotInseridos = 0;

	private int iTotAtualizados = 0;

	private int iTotRejeitados = 0;

	private JLabelPad lbAnd = new JLabelPad( "Aguardando..." );

	private JLabelPad lbInseridos = new JLabelPad( "Inseridos: 0" );

	private JLabelPad lbAtualizados = new JLabelPad( "Autualizados: 0" );

	private JLabelPad lbRejeitados = new JLabelPad( "Rejeitados: 0" );

	private ListaCampos lcFor = new ListaCampos( this );

	ProcessoSec pSec = null;

	boolean bRunProcesso = false;

	public FImpTabFor() {

		super( false );
		setTitulo( "Importação de tabela de produtos." );
		setAtribos( 50, 50, 510, 400 );

		btGerar.setToolTipText( "Executar" );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		adicBotaoSair();

		c.add( pinCab, BorderLayout.NORTH );
		c.add( spnTab, BorderLayout.CENTER );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, true ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Descrição do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setTabelaExterna( lcFor, null );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );

		pinCab.adic( new JLabelPad( "Cód.for." ), 7, 0, 300, 25 );
		pinCab.adic( txtCodFor, 7, 20, 70, 20 );
		pinCab.adic( new JLabelPad( "Descrição do fornecedor" ), 80, 0, 300, 25 );
		pinCab.adic( txtRazFor, 80, 20, 250, 20 );
		pinCab.adic( new JLabelPad( "Arquivo de dados" ), 7, 40, 200, 25 );
		pinCab.adic( txtNomeArq, 7, 60, 323, 20 );
		pinCab.adic( btBuscaArq, 330, 60, 20, 20 );
		pinCab.adic( lbAnd, 7, 100, 300, 20 );
		pinCab.adic( pbAnd, 7, 120, 323, 20 );
		pinCab.adic( btGerar, 340, 110, 30, 30 );
		pinCab.adic( lbRejeitados, 7, 140, 120, 20 );
		pinCab.adic( lbInseridos, 130, 140, 117, 20 );
		pinCab.adic( lbAtualizados, 250, 140, 120, 20 );
		pinCab.adic( new JLabelPad( "Ítens rejeitados:" ), 7, 180, 200, 20 );

		tab.adicColuna( "Motivo" );
		tab.adicColuna( "Referência" );
		tab.adicColuna( "Ref.orig." );
		tab.adicColuna( "Descrição" );
		tab.adicColuna( "Preço" );
		tab.adicColuna( "Linha" );

		tab.setTamColuna( 150, 0 );
		tab.setTamColuna( 80, 1 );
		tab.setTamColuna( 80, 2 );
		tab.setTamColuna( 250, 3 );
		tab.setTamColuna( 90, 5 );
		tab.setTamColuna( 110, 4 );

		btGerar.addActionListener( this );
		btBuscaArq.addActionListener( this );

		pbAnd.setStringPainted( true );
		pbAnd.setMinimum( 0 );
	}

	public void iniGerar() {

		if ( txtNomeArq.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Nome de arquivo em branco!" );
			return;
		}
		else if ( txtCodFor.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Código do fornecedor em branco!" );
			return;
		}
		tab.limpa();
		iAnd = 0;
		iLinha = 0;
		iTotInseridos = 0;
		iTotAtualizados = 0;
		iTotRejeitados = 0;
		state( "Iniciando processo..." );
		pSec = new ProcessoSec( 300, new Processo() {

			public void run() {

				lbAnd.updateUI();
				lbInseridos.updateUI();
				lbAtualizados.updateUI();
				lbRejeitados.updateUI();
				pbAnd.updateUI();
			}
		}, new Processo() {

			public void run() {

				btGerar.setEnabled( false );
				if ( checar() )
					gerar();
				btGerar.setEnabled( true );
				pSec.parar();
			}
		} );
		bRunProcesso = true;
		pSec.iniciar();
	}

	private boolean checar() {

		state( "Verificando arquivo..." );
		boolean bRet = false;
		File fArq = new File( txtNomeArq.getVlrString() );
		if ( fArq.exists() ) {
			if ( fArq.length() > 0 ) {
				try {
					FileReader fRead = new FileReader( fArq );
					// 143 é o tamanho max que uma linha pode chagar por isso eu passei 150:
					String sLinha[] = ( new BufferedReader( fRead, 150 ) ).readLine().split( ";" );
					if ( sLinha.length < 5 )
						throw new IOException( "Formato inválido!" );
					bRet = true;
					fRead.close();
				} catch ( IOException err ) {
					Funcoes.mensagemInforma( this, "Erro ao verificar o arquivo!\n" + err.getMessage() );
					err.printStackTrace();
				}
			}
			else
				Funcoes.mensagemInforma( this, "Arquivo vazio! " + txtNomeArq.getVlrString() );
			pbAnd.setMaximum( (int) fArq.length() );
		}
		else
			Funcoes.mensagemInforma( this, "Arquivo não existe! " + txtNomeArq.getVlrString() );
		if ( bRet )
			state( "Formato ok..." );
		else
			state( "Abortado!" );
		return bRet;
	}

	private void gerar() {

		try {
			String sLinha;
			boolean bOK = true;
			RandomAccessFile raArq = new RandomAccessFile( txtNomeArq.getVlrString(), "r" );
			state( "Iniciando gravação de registros..." );
			while ( ( sLinha = raArq.readLine() ) != null && bOK ) {
				String sVals[] = sLinha.split( ";" );
				bOK = gravaRegistro( sVals );
				iAnd = (int) raArq.getFilePointer();
				if ( !bOK ) {
					try {
						con.rollback();
					} catch ( Exception e ) {
					}
				}
				else if ( ( iLinha % 100 ) == 0 ) {
					try {
						con.commit();
					} catch ( Exception e ) {
					}
				}
				iLinha++;
			}
			raArq.close();
			try {
				con.commit();
			} catch ( Exception e ) {
			}
			state( "Pronto." );
		} catch ( IOException err ) {
			Funcoes.mensagemErro( this, "Erro ao ler os arquivo!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
			state( "Abortado!" );
		}
	}

	private boolean gravaRegistro( String[] sVals ) {

		boolean bRet = false;
		boolean bExiste = false;
		if ( critica( sVals ) ) {
			state( "Testando existência..." );
			String sSQLSelect = "SELECT REFPRODTFOR FROM CPTABFOR " + "WHERE CODEMP=? AND CODFILIAL=? AND " + "CODFOR=? AND REFPRODTFOR=?";
			try {
				PreparedStatement ps = con.prepareStatement( sSQLSelect );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "CPTABFOR" ) );
				ps.setInt( 3, txtCodFor.getVlrInteger().intValue() );
				ps.setString( 4, sVals[ 0 ].trim() );
				ResultSet rs = ps.executeQuery();
				if ( rs.next() )
					bExiste = true;
				rs.close();
				ps.close();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao verificar a existência do registro!\n" + err.getMessage(), true, con, err );
				return false;
			}
			if ( !bExiste ) {
				state( "Inserindo registro..." );
				String sSQLInsert = "INSERT INTO CPTABFOR (CODEMP,CODFILIAL,CODFOR," + "REFPRODTFOR,REFORIGTFOR,DESCPRODTFOR," + "PRECOPRODTFOR,LINHATFOR) VALUES (?,?,?,?,?,?,?,?)";
				try {
					PreparedStatement ps = con.prepareStatement( sSQLInsert );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "CPTABFOR" ) );
					ps.setInt( 3, txtCodFor.getVlrInteger().intValue() );
					ps.setString( 4, sVals[ 0 ].trim() );
					ps.setString( 5, sVals[ 1 ].trim() );
					ps.setString( 6, Funcoes.copy( sVals[ 2 ], 50 ).trim() );
					ps.setString( 7, sVals[ 3 ].replaceAll( "\\.", "" ).replace( ',', '.' ).trim() );
					ps.setString( 8, sVals[ 4 ].trim() );
					ps.execute();
					ps.close();
					iTotInseridos++;
					bRet = true;
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro ao inserir os dados!\n" + err.getMessage(), true, con, err );
				}
			}
			else {
				state( "Atualizando registro..." );
				String sSQLUpdate = "UPDATE CPTABFOR SET REFORIGTFOR=?,DESCPRODTFOR=?," + "PRECOPRODTFOR=?,LINHATFOR=? WHERE CODEMP=? AND CODFILIAL=?" + " AND CODFOR=? AND REFPRODTFOR=?";
				try {
					PreparedStatement ps = con.prepareStatement( sSQLUpdate );
					ps.setString( 1, sVals[ 1 ].trim() );
					ps.setString( 2, Funcoes.copy( sVals[ 2 ], 50 ).trim() );
					ps.setString( 3, sVals[ 3 ].replaceAll( "\\.", "" ).replace( ',', '.' ).trim() );
					ps.setString( 4, sVals[ 4 ].trim() );
					ps.setInt( 5, Aplicativo.iCodEmp );
					ps.setInt( 6, ListaCampos.getMasterFilial( "CPTABFOR" ) );
					ps.setInt( 7, txtCodFor.getVlrInteger().intValue() );
					ps.setString( 8, sVals[ 0 ].trim() );
					ps.executeUpdate();
					ps.close();
					iTotAtualizados++;
					bRet = true;
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro ao atualizar os dados!\n" + err.getMessage(), true, con, err );
				}
			}
		}
		else {
			iTotRejeitados++;
			bRet = true;
		}

		if ( bRet )
			state( "Registro processado..." );
		else
			state( "erro!" );
		return bRet;
	}

	private boolean critica( String[] sVals ) {

		boolean bRet = false;
		state( "Veficando registro..." );
		if ( sVals.length != 5 )
			tab.adicLinha( new Object[] { "N.Colunas inválido [" + iLinha + "]", "", "", "", "", "" } );
		else if ( sVals[ 0 ].trim().length() > 18 )
			tab.adicLinha( new Object[] { "Tam. Ref. maior que 18 [" + iLinha + "]", sVals[ 0 ], sVals[ 1 ], sVals[ 2 ], sVals[ 3 ], sVals[ 4 ] } );
		else if ( sVals[ 0 ].trim().length() == 0 )
			tab.adicLinha( new Object[] { "Ref. em branco! [" + iLinha + "]", sVals[ 0 ], sVals[ 1 ], sVals[ 2 ], sVals[ 3 ], sVals[ 4 ] } );
		else if ( sVals[ 1 ].trim().length() > 18 )
			tab.adicLinha( new Object[] { "Tam. Ref.orig. maior que 18 [" + iLinha + "]", sVals[ 0 ], sVals[ 1 ], sVals[ 2 ], sVals[ 3 ], sVals[ 4 ] } );
		else if ( !testaPreco( sVals[ 3 ].trim() ) )
			tab.adicLinha( new Object[] { "Preço inválido [" + iLinha + "]", sVals[ 0 ], sVals[ 1 ], sVals[ 2 ], sVals[ 3 ], sVals[ 4 ] } );
		else if ( sVals[ 4 ].trim().length() > 30 )
			tab.adicLinha( new Object[] { "Tam. Linha maior que 30 [" + iLinha + "]", sVals[ 0 ], sVals[ 1 ], sVals[ 2 ], sVals[ 3 ], sVals[ 4 ] } );
		else
			bRet = true;
		if ( bRet )
			state( "registro ok..." );
		else
			state( "erro!" );
		return bRet;
	}

	private boolean testaPreco( String sVal ) {

		boolean bRet = false;
		sVal = sVal.replaceAll( "\\.", "" ).replace( ',', '.' );
		try {
			Double.parseDouble( sVal );
			bRet = true;
		} catch ( NumberFormatException err ) {
		}
		return bRet;
	}

	private String buscaArq() {

		String sRetorno = "";
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter( Funcoes.getFiler( new String[] { "csv" } ) );
		fc.setAcceptAllFileFilterUsed( false );
		if ( fc.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {
			sRetorno = fc.getSelectedFile().getPath();
		}
		return sRetorno;
	}

	public void state( String sStatus ) {

		lbAnd.setText( sStatus );
		lbInseridos.setText( "Inseridos: " + iTotInseridos );
		lbAtualizados.setText( "Atualizados: " + iTotAtualizados );
		lbRejeitados.setText( "Rejeitados: " + iTotRejeitados );
		pbAnd.setValue( iAnd );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btGerar ) {
			iniGerar();
		}
		else if ( evt.getSource() == btBuscaArq ) {
			String sArq = buscaArq();
			if ( !sArq.equals( "" ) ) {
				txtNomeArq.setText( sArq );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFor.setConexao( cn );
	}
}
