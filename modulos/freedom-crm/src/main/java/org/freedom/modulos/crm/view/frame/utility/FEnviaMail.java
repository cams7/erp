/**
 * @version 25/09/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FEnviaMail.java <BR>
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

package org.freedom.modulos.crm.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.crm.FPrefere;

/**
 * @author robson
 * 
 *         To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FEnviaMail extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodAtiv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAtiv = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtArqMen = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtAssunto = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtDe = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JButtonPad btBuscaArq = new JButtonPad( Icone.novo( "btAbrirPeq.png" ) );

	private JButtonPad btEnviar = new JButtonPad( Icone.novo( "btEnviarMail.png" ) );

	private JTextAreaPad txaMen = new JTextAreaPad();

	private JScrollPane spnMen = new JScrollPane( txaMen );

	private JPanelPad pinGeral = new JPanelPad( 0, 145 );

	private JPanelPad pinArq = new JPanelPad( 0, 90 );

	private JPanelPad pinRod = new JPanelPad( 0, 0 );

	private JPanelPad pnCenter = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JProgressBar pbAnd = new JProgressBar();

	private JLabelPad lbStatus = new JLabelPad( "Pronto." );

	private JRadioGroup<?, ?> rgTipo = null;

	private ListaCampos lcAtiv = new ListaCampos( this, "" );

	String sSMTP = null;

	String sUser = null;

	String sPass = null;

	File fArq = null;

	boolean bEnvia = false;

	public FEnviaMail() {

		super( false );
		setTitulo( "Envio de e-mail aos contatos" );
		setAtribos( 100, 100, 375, 430 );

		lbStatus.setPreferredSize( new Dimension( 0, 20 ) );
		lbStatus.setForeground( Color.BLUE );
		pinRod.tiraBorda();

		Vector<String> vVals = new Vector<String>();
		vVals.add( "A" );
		vVals.add( "C" );
		Vector<String> vLabs = new Vector<String>();
		vLabs.add( "Arquivo no anexo." );
		vLabs.add( "Arquivo no corpo." );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setBorder( BorderFactory.createEmptyBorder() );

		lbStatus.setBorder( BorderFactory.createEtchedBorder() );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pinGeral, BorderLayout.NORTH );
		c.add( pnCenter, BorderLayout.CENTER );
		c.add( pinRod, BorderLayout.SOUTH );

		adicBotaoSair().add( pinRod, BorderLayout.CENTER );

		pnCenter.add( pinArq, BorderLayout.NORTH );
		pnCenter.add( spnMen, BorderLayout.CENTER );
		pnCenter.add( lbStatus, BorderLayout.SOUTH );

		txtCodAtiv.setTipo( JTextFieldPad.TP_INTEGER, 8, 0 );
		txtDescAtiv.setTipo( JTextFieldPad.TP_STRING, 40, 0 );
		lcAtiv.add( new GuardaCampo( txtCodAtiv, "CodAtiv", "Cód.ativ.", ListaCampos.DB_PK, false ) );
		lcAtiv.add( new GuardaCampo( txtDescAtiv, "DescAtiv", "Descrição da atividade", ListaCampos.DB_SI, false ) );
		txtCodAtiv.setTabelaExterna( lcAtiv, null );
		txtCodAtiv.setNomeCampo( "CodAtiv" );
		txtCodAtiv.setFK( true );
		lcAtiv.setReadOnly( true );
		lcAtiv.montaSql( false, "ATIVIDADE", "TK" );

		pinGeral.adic( new JLabelPad( "Cód.ativ." ), 7, 0, 290, 20 );
		pinGeral.adic( txtCodAtiv, 7, 20, 80, 20 );
		pinGeral.adic( new JLabelPad( "Descrição da atividade" ), 90, 0, 290, 20 );
		pinGeral.adic( txtDescAtiv, 90, 20, 250, 20 );
		pinGeral.adic( new JLabelPad( "De:" ), 7, 40, 333, 20 );
		pinGeral.adic( txtDe, 7, 60, 333, 20 );
		pinGeral.adic( new JLabelPad( "Assunto:" ), 7, 80, 333, 20 );
		pinGeral.adic( txtAssunto, 7, 100, 333, 20 );

		pinArq.adic( new JLabelPad( "Arquivo" ), 7, 0, 200, 20 );
		pinArq.adic( txtArqMen, 7, 20, 313, 20 );
		pinArq.adic( btBuscaArq, 320, 20, 20, 20 );
		pinArq.adic( rgTipo, 7, 45, 333, 30 );

		pinRod.adic( btEnviar, 7, 0, 30, 30 );
		pinRod.adic( pbAnd, 45, 5, 205, 20 );

		pbAnd.setStringPainted( true );
		pbAnd.setMinimum( 0 );

		btBuscaArq.addActionListener( this );
		btEnviar.addActionListener( this );
	}

	private String buscaArq() {

		String sRetorno = "";
		JFileChooser fcImagem = new JFileChooser();
		if ( fcImagem.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {
			sRetorno = fcImagem.getSelectedFile().getPath();
		}
		return sRetorno;
	}

	private void abrePrefere() {

		if ( !fPrim.temTela( "Pref. Gerais" ) ) {
			FPrefere tela = new FPrefere();
			fPrim.criatela( "Pref. Gerais", tela, con );
		}
	}

	private boolean verifSmtp() {

		boolean bRet = false;
		String sSQL = "SELECT SMTPMAIL,USERMAIL,PASSMAIL FROM SGPREFERE3 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				sSMTP = rs.getString( "SMTPMail" ) != null ? rs.getString( "SMTPMail" ).trim() : null;
				sUser = rs.getString( "UserMail" ) != null ? rs.getString( "UserMail" ).trim() : null;
				sPass = rs.getString( "PassMail" ) != null ? rs.getString( "PassMail" ).trim() : null;
			}
			if ( sSMTP == null ) {
				Funcoes.mensagemInforma( null, "Não foi cadastrado o servidor de SMTP!\nUtilize a tela de preferências gerais para configurar." );
			}
			else
				bRet = true;
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o as informações de SMTP!\n" + err.getMessage() );
			err.printStackTrace();
		}
		return bRet;
	}

	private boolean verifCab() {

		boolean bRet = false;
		if ( txtDe == null ) {
			Funcoes.mensagemInforma( this, "Não foi preenchido o campo 'De:'!" );
		}
		else if ( txtAssunto == null ) {
			Funcoes.mensagemInforma( this, "Não foi preenchido o campo 'Assunto:'!" );
		}
		else
			bRet = true;
		return bRet;
	}

	private boolean verifCorpo() {

		boolean bRet = false;
		if ( txaMen.getVlrString().equals( "" ) && txtArqMen.getVlrString().equals( "" ) )
			Funcoes.mensagemInforma( this, "Nada a ser enviado!" );
		else
			bRet = true;
		return bRet;
	}

	private void enviar() {

		String sWhere = "";
		int iConta = 0;
		if ( !verifSmtp() )
			return;
		else if ( !verifCab() )
			return;
		else if ( !verifCorpo() )
			return;
		Properties pp = new Properties();
		pp.put( "mail.smtp.host", sSMTP );
		Session se = Session.getDefaultInstance( pp );

		pbAnd.setMaximum( 1 );
		pbAnd.setValue( iConta = 0 );

		fArq = null;
		if ( !txtArqMen.getVlrString().equals( "" ) ) {
			fArq = new File( txtArqMen.getVlrString() );
			if ( !fArq.exists() ) {
				Funcoes.mensagemErro( this, "Arquivo não foi encotrado!" );
				return;
			}
		}

		String sSQL = "SELECT NOMECTO,EMAILCTO FROM TKCONTATO WHERE CODEMP=? AND CODFILIAL=?" + sWhere;
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "TKCONTATO" ) );
			ResultSet rs = ps.executeQuery();
			bEnvia = true;
			while ( rs.next() ) {
				String sEmail = rs.getString( "EmailCto" );
				if ( !bEnvia )
					break;
				if ( sEmail != null ) {
					if ( rs.getString( "NomeCto" ) != null )
						sEmail = rs.getString( "NomeCto" ).trim() + " <" + sEmail.trim() + '>';
					else
						sEmail = sEmail.trim();
					mandaMail( sEmail, se );
					pbAnd.setValue( ++iConta );
				}
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar contatos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

	}

	private void mandaMail( String sTo, Session se ) {

		state( "Preparando envio para: '" + sTo + "'" );
		try {

			MimeMessage mes = new MimeMessage( se );
			mes.setFrom( new InternetAddress( txtDe.getVlrString() ) );
			mes.setSubject( txtAssunto.getVlrString() );
			mes.setSentDate( new Date() );
			mes.addRecipient( RecipientType.TO, new InternetAddress( sTo ) );

			BodyPart parte = new MimeBodyPart();

			parte.setText( txaMen.getVlrString() );

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart( parte );

			if ( fArq != null ) {
				parte = new MimeBodyPart();
				FileDataSource orig = new FileDataSource( fArq );
				parte.setDataHandler( new DataHandler( orig ) );
				parte.setFileName( fArq.getName() );
				parte.setDisposition( rgTipo.getVlrString().equals( "A" ) ? Part.ATTACHMENT : Part.INLINE );
				multipart.addBodyPart( parte );
			}
			mes.setContent( multipart );
			state( "Enviando dados..." );
			Transport.send( mes );
			state( "Envio OK..." );
		} catch ( MessagingException err ) {
			Funcoes.mensagemErro( this, "Erro ao enviar mensagem para: " + sTo + "\n" + err.getMessage() );
			err.getStackTrace();
			state( "Aguardando reenvio." );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		String sArq = "";
		if ( evt.getSource() == btBuscaArq ) {
			sArq = buscaArq();
			if ( !sArq.equals( "" ) ) {
				txtArqMen.setText( sArq );
			}
		}
		else if ( evt.getSource() == btEnviar ) {
			ProcessoSec pSec = new ProcessoSec( 500, new Processo() {

				public void run() {

					lbStatus.updateUI();
					pbAnd.updateUI();
				}
			}, new Processo() {

				public void run() {

					enviar();
				}
			} );
			pSec.iniciar();
		}
	}

	public void state( String sStatus ) {

		lbStatus.setText( sStatus );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAtiv.setConexao( cn );
	}
}
