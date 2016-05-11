/**
 * @version 10/02/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLBaixaRec.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Tela para captura da pesagem de cargas.
 * 
 */

package org.freedom.modulos.gms.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Date;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.EditEvent;
import org.freedom.bmps.Icone;
import org.freedom.infra.comm.CtrlPort;
import org.freedom.infra.driver.scale.AbstractScale;
import org.freedom.infra.driver.scale.ScaleResult;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPassword;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.gms.business.object.TipoRecMerc;

public class DLPesagem extends FFDialogo implements CarregaListener, FocusListener, SerialPortEventListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtPeso1 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 0 );

	private JTextFieldPad txtPeso2 = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 0 );

	private JTextFieldPad txtData = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtHora = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );

	private JLabelPad lbpeso1 = new JLabelPad( ( "Peso 1" ) );

	private JLabelPad lbpeso2 = new JLabelPad( ( "Peso 2" ) );

	private SerialPort porta = null;

	private AbstractScale balanca = null;

	private String tipoprocrecmerc = null;

	private String driverbal = null;

	private Integer portabal = null;

	private Integer timeout = null;

	private Integer baundrate = null;

	private Integer databits = null;

	private Integer stopbits = null;

	private Integer parity = null;

	public Container c = getContentPane();

	private JPanelPad pnGeral = new JPanelPad( new BorderLayout() );

	private JPanelPad pnCampos = new JPanelPad();

	private JPanelPad pnMensagens = new JPanelPad( 280, 28 );

	private JLabelPad mensagem = new JLabelPad( "" );

	private JButtonPad btRefazer = new JButtonPad( "Refazer", Icone.novo( "btsoliccp.png" ) );

	private boolean contingencia = false;

	public DLPesagem( Component cOrig, String tipoprocrecmerc, boolean contingencia ) {

		super( cOrig );

		this.contingencia = contingencia;
		this.tipoprocrecmerc = tipoprocrecmerc;

		montatela();

		adicListeners();

	}

	private void montatela() {

		int irow = 0; // Variavel para salto na posição do campo de data e hora

		c.add( pnGeral, BorderLayout.CENTER );

		pnGeral.add( pnCampos, BorderLayout.CENTER );
		pnGeral.add( pnMensagens, BorderLayout.SOUTH );

		// mensagem.setBackground( Color.RED );

		setMensagem( "Iniciando parâmetros...", Color.BLUE, null, false, null );

		// pnMensagens.add( mensagem, BorderLayout.WEST );

		pnMensagens.adic( mensagem, 6, 0, 280, 24 );

		pnCampos.adic( lbpeso1, 7, 0, 252, 20 );
		pnCampos.adic( txtPeso1, 7, 20, 252, 50 );

		if ( TipoRecMerc.PROCESSO_DESCARREGAMENTO.getValue().equals( tipoprocrecmerc ) ) {

			pnCampos.adic( lbpeso2, 7, 80, 252, 20 );
			pnCampos.adic( txtPeso2, 7, 100, 252, 50 );

			irow = 80;
		}

		setAtribos( 280, 280 + irow );

		pnCampos.adic( new JLabelPad( "Data" ), 7, 80 + irow, 135, 20 );
		pnCampos.adic( txtData, 7, 100 + irow, 135, 50 );

		pnCampos.adic( new JLabelPad( "Hora" ), 147, 80 + irow, 110, 20 );
		pnCampos.adic( txtHora, 147, 100 + irow, 110, 50 );

	}

	private void adicListeners() {

		btRefazer.addActionListener( this );
		btRefazer.setText( "Nova captura - (F12)" );
		txtPeso1.addKeyListener( this );
		txtPeso2.addKeyListener( this );
		btRefazer.addKeyListener( this );
		addKeyListener( this );

	}

	private void setMensagem( String msg, Color cortexto, Color corpainel, boolean refazer, String titulobotao ) {

		if ( cortexto != null ) {
			mensagem.setForeground( cortexto );
		}
		else {
			mensagem.setForeground( Color.BLACK );
		}

		if ( corpainel != null ) {
			pnMensagens.setBackground( corpainel );
		}
		else {
			pnMensagens.setBackground( Color.GRAY );
		}

		mensagem.setText( msg );

		if ( refazer ) {

			pnGrid.removeAll();

			pnGrid.add( btRefazer );
			pnGrid.add( btCancel );

			btRefazer.setVisible( true );

		}
		else {
			pnGrid.removeAll();

			btRefazer.setVisible( false );

			pnGrid.add( btOK );
			pnGrid.add( btCancel );

		}

		System.out.println( "mensagem:" + msg );
		if ( titulobotao != null ) {
			btRefazer.setText( "2a pesagem" );
		}

	}

	private synchronized void buscaPeso() {

		try {

			if ( balanca != null ) {

				setMensagem( "Inicializando balança...", Color.BLUE, null, false, null );

				Date data = null;
				Time hora = null;
				BigDecimal peso = null;

				if ( !contingencia ) {

					balanca.initialize( portabal, AbstractScale.TIMEOUT_ACK, baundrate, databits, stopbits, parity );
					// Usar thred apenas para balanca bci.
					Thread t = null;
					if ( balanca.isBufferized() ) {
						t = new Thread( balanca );
						t.start();

						Thread.sleep( 1000 );
					}

					setMensagem( "Recuperando dados da balança...", Color.BLUE, null, false, null );

					balanca.readstring = false;

					//Thread.sleep( 500 );
					
					ScaleResult scaleresult = balanca.parseString();
					
					for (int i=0; i<10; i++) {
						if ( scaleresult!=null) {
							break;
						}
						Thread.sleep( 1000 );
						scaleresult = balanca.parseString();
						if (scaleresult!=null) {
							System.out.println("Peso no loop: "+i+" = "+scaleresult.getWeight());
						}
					}

					if (scaleresult==null) {
						Funcoes.mensagemInforma( this, "Não foi possível obter pesso da balança após 10 tentativas!" );
					} else {
						data = scaleresult.getDate();
						hora = scaleresult.getTime();
						peso = scaleresult.getWeight();
					}
/*					data = balanca.getDate();
					hora = balanca.getTime();
					peso = balanca.getWeight();
*/
				}
				// Modo de contingencia ativado
				else {

					balanca.inactivePort();

					data = new Date();
					hora = new Time( new Date().getTime() );
					peso = new BigDecimal( 0 );

				}

				if ( peso != null ) {
					if ( txtPeso1.getVlrBigDecimal().floatValue() > 0 && tipoprocrecmerc.equals( TipoRecMerc.PROCESSO_DESCARREGAMENTO.getValue() ) ) {
						txtPeso2.setVlrBigDecimal( peso );
					}
					else {
						txtPeso1.setVlrBigDecimal( peso );
					}

				}
				else {
					setMensagem( "Peso inválido na pesagem!", Color.WHITE, Color.RED, true, "Refazer" );
					return;
				}
				if ( data != null ) {
					txtData.setVlrDate( data );
				}
				else {
					setMensagem( "Data inválida na pesagem!", Color.ORANGE, null, false, "Refazer" );
					return;
				}

				if ( hora != null ) {
					txtHora.setVlrTime( hora );
				}
				else {
					setMensagem( "Hora inválida na pesagem!", Color.ORANGE, null, false, "Refazer" );
					return;
				}

				if ( txtPeso1.getVlrBigDecimal().floatValue() > 0 && txtPeso2.getVlrBigDecimal().floatValue() == 0 && tipoprocrecmerc.equals( TipoRecMerc.PROCESSO_DESCARREGAMENTO.getValue() ) ) {
					setMensagem( "Aguardando segunda pesagem...", Color.WHITE, Color.orange, true, "2a. Pesagem" );
				}
				else {
					setMensagem( "Pesagem realizada com sucesso!", Color.WHITE, new Color( 0, 128, 0 ), false, null );
				}

				// balanca.inactivePort();
				// balanca = null;

			}
			else {
				setMensagem( "Balança não inicializada!", Color.WHITE, Color.RED, true, "Refazer" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void buscaParansBalanca() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sql.append( "select first 1 driverbal, portabal, speedbal, bitsbal, stopbitbal, paritybal " );
			sql.append( "from sgestacaobal " );
			sql.append( "where codemp=? and codfilial=? and codest=? and tipoprocrecmerc=? " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, Aplicativo.iNumEst );
			ps.setString( 4, tipoprocrecmerc );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				driverbal = rs.getString( "driverbal" );
				portabal = rs.getInt( "portabal" );
				baundrate = rs.getInt( "speedbal" );
				databits = rs.getInt( "bitsbal" );
				stopbits = rs.getInt( "stopbitbal" );
				parity = rs.getInt( "paritybal" );

			}
			else {
				setMensagem( "Balança não localizada!", Color.RED, null, false, null );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@ SuppressWarnings ( "unchecked" )
	private void instanciaBalanca() {

		try {

			if ( balanca != null ) {
				balanca.inactivePort();
			}

			buscaParansBalanca();

			Class<AbstractScale> classbalanca = ( (Class<AbstractScale>) Class.forName( StringFunctions.alltrim( driverbal ) ) );

			balanca = classbalanca.newInstance();

			setMensagem( "Balança " + balanca.getName(), Color.BLUE, null, false, null );

			buscaPeso();

		} catch ( Exception e ) {
			setMensagem( "Erro ao instanciar driver... ", Color.RED, null, false, null );
			e.printStackTrace();
		}

	}

	private void ajustaCampos() {

		txtData.setEditable( false );
		txtHora.setEditable( false );

		// Liberado para testes
		// txtPeso1.setAtivo( balanca==null );
		// txtPeso2.setAtivo( balanca==null );

		txtPeso1.setEditable( false );
		txtPeso2.setEditable( false );

		txtPeso1.setFont( SwingParams.getFontboldextra( 20 ) );
		txtPeso2.setFont( SwingParams.getFontboldextra( 20 ) );
		txtData.setFont( SwingParams.getFontboldextra( 10 ) );
		txtHora.setFont( SwingParams.getFontboldextra( 10 ) );

	}

	public BigDecimal getPeso1() {

		return txtPeso1.getVlrBigDecimal();
	}

	public BigDecimal getPeso2() {

		return txtPeso2.getVlrBigDecimal();
	}

	public Date getData() {

		return txtData.getVlrDate();
	}

	public String getHora() {

		return txtHora.getVlrString();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			super.actionPerformed( evt );
		}
		else if ( evt.getSource() == btRefazer ) {
			System.out.println( "Acionado o botão refazer..." );
			instanciaBalanca();
		}
		else {
			super.actionPerformed( evt );
		}
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void focusLost( FocusEvent fevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void beforeEdit( EditEvent eevt ) {

	}

	public void afterEdit( EditEvent eevt ) {

	}

	private void abrePorta() {

		try {

			CommPortIdentifier cp = CommPortIdentifier.getPortIdentifier( "/dev/ttyS0" );
			porta = (SerialPort) cp.open( "SComm", 1 );

			InputStream entrada = porta.getInputStream();

			porta.notifyOnDataAvailable( true );

			porta.addEventListener( this );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	public void serialEvent( SerialPortEvent ev ) {

		try {

			if ( ev.getEventType() == SerialPortEvent.DATA_AVAILABLE ) {

				InputStream entrada = porta.getInputStream();
				int nodeBytes = 0;

				byte[] bufferLeitura = new byte[ 64 ];

				while ( entrada.available() > 0 ) {

					nodeBytes = entrada.read( bufferLeitura );

				}

				String leitura = new String( bufferLeitura );

				if ( bufferLeitura.length > 0 ) {
					System.out.print( leitura );
					parseString( leitura );
				}
				else {
					System.out.print( "nada lido!" );
				}

				System.out.println( "número de bytes lidos:" + nodeBytes );

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private void parseString( String str ) {

		String strweight = "";
		String strdate = "";
		String strtime = "";

		try {

			int posicaobranca = str.lastIndexOf( " " );

			if ( posicaobranca < 24 && posicaobranca > -1 ) {
				str = str.substring( posicaobranca );
				str = StringFunctions.alltrim( str );
			}

			if ( str.length() >= 24 ) {

				if ( posicaobranca + 24 > str.length() ) {
					str = str.substring( posicaobranca );
				}
				else {
					str = str.substring( posicaobranca, 24 );
				}

				str = StringFunctions.alltrim( str );

				System.out.println( "Parcial analisada: " + str );

				String validador = str.substring( 11, 12 ) + str.substring( 14, 15 ) + str.substring( 19, 20 );

				if ( "//:".equals( validador ) ) {

					CtrlPort.getInstance().disablePort();

					// InputStream input = null;
					// CtrlPort ctrlport = CtrlPort.getInstance();
					// ctrlport.setActive(false);
					// input = ctrlport.getInput();

					// input.close();

					// ctrlport.disablePort();
					// ctrlport = null;

					// buffer = null;

					System.out.println( "Finalizou leitura e fechou a porta!" );

					strweight = str.substring( 0, 07 );
					strdate = str.substring( 9, 17 );
					strtime = str.substring( 17 );

					strtime = StringFunctions.clearString( strtime );

					System.out.println( "peso:" + ConversionFunctions.stringToBigDecimal( strweight ) );
					System.out.println( "data:" + ConversionFunctions.strDate6digToDate( strdate ) );
					System.out.println( "data:" + ConversionFunctions.strTimetoTime( strtime ) );
					// setWeight( ConversionFunctions.stringToBigDecimal( strweight ) );
					// setDate( ConversionFunctions.strDate6digToDate( strdate ) );
					// setTime( ConversionFunctions.strTimetoTime( strtime ) );

				}

			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		instanciaBalanca();
		// abrePorta();

		ajustaCampos();

	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_F12 ) {
			if ( btRefazer.isVisible() ) {
				btRefazer.doClick();
			}
		}
		if ( kevt.getKeyCode() == KeyEvent.VK_F11 ) {

			liberaCampo( true );
		}
		// ((Component)kevt.getSource()).addKeyListener( this );
		super.keyPressed( kevt );
	}

	private void liberaCampo( boolean libera ) {

		FPassword fpw = new FPassword( this, FPassword.LIBERA_CAMPO_PESAGEM, null, con );
		fpw.execShow();

		if ( fpw.OK ) {
			txtPeso1.setEditable( libera );
			txtPeso2.setEditable( libera );
			txtData.setEditable( libera );
			txtHora.setEditable( libera );

			contingencia = true;

		}

		fpw.dispose();

	}

}
