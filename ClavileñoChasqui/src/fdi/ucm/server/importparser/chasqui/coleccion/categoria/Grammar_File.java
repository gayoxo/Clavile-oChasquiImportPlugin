/**
 * 
 */
package fdi.ucm.server.importparser.chasqui.coleccion.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementFile;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;

/**
 * Clase que implementa la creacion de los files y de sus atributos.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class Grammar_File implements InterfaceChasquiparser{

	private CompleteGrammar Attributo;
//	private MetaText PATH;
	private CompleteResourceElementType FireRef;
	private LoadCollectionChasqui LCole;
	private static final String CATEGORIAS_VACIAS = " Existen filas con categorias vacias";
	private static CompleteLinkElementType IdovOwner;
	
	public Grammar_File(CompleteCollection Padre,LoadCollectionChasqui lcole) {
		LCole=lcole;
		{
			Attributo = new CompleteGrammar(NameConstantsChasqui.FILENAME,NameConstantsChasqui.FILENAME,Padre);
			CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			VistaOV.getValues().add(Valor);
			VistaOV.getValues().add(Valor2);
			VistaOV.getValues().add(Valor3);
			
			CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsChasqui.META);
			
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.FILE,VistaOVMeta);

			VistaOVMeta.getValues().add(ValorMeta);
			
			Attributo.getViews().add(VistaOVMeta);
			Attributo.getViews().add(VistaOV);
		}


		{
			IdovOwner = new CompleteLinkElementType(NameConstantsChasqui.IDOV_OWNERNAME, Attributo);
			Attributo.getSons().add(IdovOwner);
			
			CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			VistaOV.getValues().add(Valor);
			VistaOV.getValues().add(Valor2);
			VistaOV.getValues().add(Valor3);
			
			CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsChasqui.META);
			
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.IDOV_OWNER,VistaOVMeta);

			VistaOVMeta.getValues().add(ValorMeta);
			
			IdovOwner.getShows().add(VistaOVMeta);
			
			IdovOwner.getShows().add(VistaOV);
		}
		{
			FireRef = new CompleteResourceElementType(NameConstantsChasqui.FILENAME, Attributo);
			Attributo.getSons().add(FireRef);
			
			CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			VistaOV.getValues().add(Valor);
			VistaOV.getValues().add(Valor2);
			VistaOV.getValues().add(Valor3);
			
			CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsChasqui.META);
			
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.FILERESOURCE,VistaOVMeta);

			VistaOVMeta.getValues().add(ValorMeta);
			
			FireRef.getShows().add(VistaOVMeta);
			FireRef.getShows().add(VistaOV);
		}
	}

	@Override
	public void ProcessAttributes() {
		
	}

	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT idov,nom_rec,descripcion FROM recursos WHERE tipo!='OV' AND ruta='/';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Padre=rs.getObject("idov").toString();
					String Nombre=rs.getObject("nom_rec").toString();
					String Descripcion="";
					if (rs.getObject("descripcion")!=null)
						Descripcion=rs.getObject("descripcion").toString().trim();
					
					if (Padre!=null&&!Padre.isEmpty()&&Nombre!=null&&!Nombre.isEmpty())
						{
						Integer Idov = Integer.parseInt(Padre);	
						StringBuffer SB=new StringBuffer();
						if (LCole.getBaseURLChasqui().isEmpty()||
								(!LCole.getBaseURLChasqui().startsWith("http://")
										&&!LCole.getBaseURLChasqui().startsWith("https://")
										&&!LCole.getBaseURLChasqui().startsWith("ftp://")))
							SB.append("http://");
						SB.append(LCole.getBaseURLChasqui());
						if (!LCole.getBaseURLChasqui().isEmpty()&&!LCole.getBaseURLChasqui().endsWith("/"))
							SB.append("/");
						
						SB.append(Idov+"/"+Nombre.toLowerCase());
						String Path=SB.toString();
						String Path2 = Idov+"/"+Nombre.toLowerCase();
						CompleteFile EFV=new CompleteFile(Path,LCole.getCollection().getChasquiCollection());
							
						CompleteDocuments FileConst= new CompleteDocuments(LCole.getCollection().getChasquiCollection(),Attributo,"",Path);
						
						if (!Descripcion.isEmpty())
							FileConst.setDescriptionText(Descripcion);
							else FileConst.setDescriptionText(Path);
//						FileConst.setIcon(Path);
						
						CompleteResourceElement MR=new CompleteResourceElementFile(this.FireRef, EFV);
						FileConst.getDescription().add(MR);
						
						LCole.getCollection().getChasquiCollection().getSectionValues().add(EFV);
						LCole.getCollection().getChasquiCollection().getEstructuras().add(FileConst);
						LCole.getCollection().getFiles().put(Path2, EFV);
						LCole.getCollection().getFilesC().put(Path2,FileConst);

						}
					else System.err.println(CATEGORIAS_VACIAS);

				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @return the attributo
	 */
	public CompleteGrammar getAttributo() {
		return Attributo;
	}

	/**
	 * @param attributo the attributo to set
	 */
	public void setAttributo(CompleteGrammar attributo) {
		Attributo = attributo;
	}

	/**
	 * @return the idovOwner
	 */
	public static CompleteLinkElementType getIdovOwner() {
		return IdovOwner;
	}

	/**
	 * @param idovOwner the idovOwner to set
	 */
	public static void setIdovOwner(CompleteLinkElementType idovOwner) {
		IdovOwner = idovOwner;
	}

	
}
