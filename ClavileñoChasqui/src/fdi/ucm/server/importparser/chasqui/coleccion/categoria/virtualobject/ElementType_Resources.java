package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject;

import java.sql.ResultSet;
import java.sql.SQLException;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.Grammar_File;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteLinkElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteOperationalValue;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteStructure;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que implementa la referencia.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Resources implements InterfaceChasquiparser {
	
	private CompleteGrammar metaPadre;
	private CompleteLinkElementType Attributo;
	private CompleteTextElementType Descripcion;
	private CompleteTextElementType DisplayNeme;
	private CompleteTextElementType OldSystemID;
	private CompleteTextElementType TypoString;
	private CompleteTextElementType TypoRecString;
	private CompleteOperationalValueType VisibleDesc;
	private CompleteOperationalValueType VisibleDisp;
	private CompleteOperationalValueType VisibleIDname;
	private CompleteOperationalValueType VisibleAtt;
	private CompleteOperationalValueType VisibleTypoString;
	private CompleteOperationalValueType VisibleTypoRecString;
	private LoadCollectionChasqui LCole;
	private static final String CATEGORIAS_VACIAS = " Existen filas con categorias vacias";



	public ElementType_Resources(CompleteGrammar meta,LoadCollectionChasqui lcole) {
		LCole=lcole;
		metaPadre=meta;
	}


	@Override
	public void ProcessAttributes() {
		{
			Attributo = new CompleteLinkElementType(NameConstantsChasqui.RESOURCENAME,metaPadre);		
			
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			VisibleAtt=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			Attributo.getShows().add(VisibleAtt);
			Attributo.getShows().add(Valor2);
			Attributo.getShows().add(Valor3);
			
			String VistaOVMeta=new String(NameConstantsChasqui.META);
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.RESOURCE,VistaOVMeta);
			Attributo.getShows().add(ValorMeta);
			

		}

		{
			Descripcion = new CompleteTextElementType(NameConstantsChasqui.DESCRIPTIONNAME,Attributo,Attributo.getCollectionFather());
			Attributo.getSons().add(Descripcion);
			
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			VisibleDesc=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			Descripcion.getShows().add(VisibleDesc);
			Descripcion.getShows().add(Valor2);
			Descripcion.getShows().add(Valor3);

			
			String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
			 CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.TEXT,VistaMetaType);
			 Descripcion.getShows().add(MetaType);
		}

		{
			DisplayNeme = new CompleteTextElementType(NameConstantsChasqui.DISPLAY_NAMENAME,Attributo,Attributo.getCollectionFather());
			Attributo.getSons().add(DisplayNeme);
			
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			VisibleDisp=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			DisplayNeme.getShows().add(VisibleDisp);
			DisplayNeme.getShows().add(Valor2);
			DisplayNeme.getShows().add(Valor3);
			
			String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
			 CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.TEXT,VistaMetaType);
			 DisplayNeme.getShows().add(MetaType);
		}


		{
			OldSystemID = new CompleteTextElementType(NameConstantsChasqui.IDNAME, Attributo,Attributo.getCollectionFather());
			Attributo.getSons().add(OldSystemID);

			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			VisibleIDname=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			OldSystemID.getShows().add(VisibleIDname);
			OldSystemID.getShows().add(Valor2);
			OldSystemID.getShows().add(Valor3);
			
			
			
			String VistaOV2=new String(NameConstantsChasqui.METATYPE);
			 CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.NUMERIC,VistaOV2);
			 OldSystemID.getShows().add(Valor);
			
		}

		ElementType_Resources_Type ATTM = new ElementType_Resources_Type(
				NameConstantsChasqui.TYPE_FILENAME, false, Attributo,LCole);
		ATTM.ProcessAttributes();
		
		{
			
		TypoString = ATTM.getExtendMetaControlled();
		Attributo.getSons().add(TypoString);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		VisibleTypoString=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		TypoString.getShows().add(VisibleTypoString);
		TypoString.getShows().add(Valor2);
		TypoString.getShows().add(Valor3);

		}
		
		ElementType_Resources_TypeRec ATTM2 = new ElementType_Resources_TypeRec(
				NameConstantsChasqui.TYPE_FILE_RECNAME, false, Attributo,LCole);
		ATTM2.ProcessAttributes();
		
		{
		TypoRecString = ATTM2.getExtendMetaControlled();
		Attributo.getSons().add(TypoRecString);
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		VisibleTypoRecString=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		TypoRecString.getShows().add(VisibleTypoRecString);
		TypoRecString.getShows().add(Valor2);
		TypoRecString.getShows().add(Valor3);

		
		}
		

	}

	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM recursos ORDER BY idov;");
			if (rs!=null) 
			{
				Integer preIdov=null;
				int MaxCount=0;
				int count=1;
				while (rs.next()) {
					
					String IdS=rs.getObject("id").toString();
					String Padre=rs.getObject("idov").toString();
					String Nombre=rs.getObject("nom_rec").toString();
					Object nulable = rs.getObject("descripcion");
					String ruta=rs.getObject("ruta").toString();
					String Descripcion="";
					if (nulable!=null)
						Descripcion=nulable.toString();
					Object nulable2 = rs.getObject("nom_rec_publico");
					String displayName=Nombre;
					if (nulable2!=null)
						displayName=nulable2.toString();
					Object nulable3 = rs.getObject("visible");
					String visible="NO";
					if (nulable3!=null)
						visible=nulable3.toString();
					String IdovRef=ruta.substring(0, ruta.length()-1);
					//
					Object typonull = rs.getObject("tipo");
					String typoString="";
					if (typonull!=null)
						typoString=typonull.toString();
					//
					Object typoRec = rs.getObject("tipoRec");
					String typoRecString="";
					if (typoRec!=null)
						typoRecString=typoRec.toString();
					
					if (Padre!=null&&!Padre.isEmpty()&&Nombre!=null&&!Nombre.isEmpty()&&IdovRef!=null)
						{
						Integer Idov = Integer.parseInt(Padre);	
						if (preIdov!=null&&preIdov.intValue()==Idov.intValue())
							count++;
						else
							{
							if (count>MaxCount)
								MaxCount=count;
							preIdov=Idov;
							count=1;
							}
						
						boolean visiblebol;
						if (visible.toLowerCase().equals("SI".toLowerCase()))
							visiblebol=true;
					else visiblebol=false;
						if (displayName.isEmpty()) displayName=Nombre;
							
						CompleteDocuments R=LCole.getCollection().getObjetoVirtual().get(Idov);
						
						
						CompleteDocuments EFV;
						if (typoString.equals("OV"))
									{
									Integer Idovrefe = Integer.parseInt(IdovRef);
									EFV=LCole.getCollection().getObjetoVirtual().get(Idovrefe);	
									}
						else
							if (IdovRef.isEmpty())
							{
								CompleteDocuments EFVF=LCole.getCollection().getFilesC().get(Idov+"/"+Nombre.toLowerCase());
							 	CompleteLinkElement AtributoBaseRelacion = new CompleteLinkElement(Grammar_File.getIdovOwner(), R);
							 	EFVF.getDescription().add(AtributoBaseRelacion);
							 	EFV=EFVF;
							}
							else
								EFV=LCole.getCollection().getFilesC().get(IdovRef+"/"+Nombre.toLowerCase());
								
						
						
							
						{
							CompleteLinkElement AtributoBaseRelacion2 = new CompleteLinkElement(Attributo, EFV);
						AtributoBaseRelacion2.getAmbitos().add(count);
						R.getDescription().add(AtributoBaseRelacion2);

						CompleteOperationalValue Valor=new CompleteOperationalValue(VisibleAtt,Boolean.toString(visiblebol));

						AtributoBaseRelacion2.getShows().add(Valor);
						}
						
						if (!Descripcion.trim().isEmpty()) {
							CompleteTextElement DescripcionTexto = new CompleteTextElement(this.Descripcion,Descripcion);
							DescripcionTexto.getAmbitos().add(count);
							R.getDescription().add(DescripcionTexto);
							
							CompleteOperationalValue Valor=new CompleteOperationalValue(VisibleDesc,Boolean.toString(visiblebol));

							DescripcionTexto.getShows().add(Valor);
							
						}
						
						{
						CompleteTextElement NombreEnPantalla = new CompleteTextElement(this.DisplayNeme,displayName);
						NombreEnPantalla.getAmbitos().add(count);
						R.getDescription().add(NombreEnPantalla);
						
						CompleteOperationalValue Valor=new CompleteOperationalValue(VisibleDisp,Boolean.toString(visiblebol));

						NombreEnPantalla.getShows().add(Valor);
						}

						
							
						{
						CompleteTextElement IdEnViejoSistema = new CompleteTextElement(this.OldSystemID,IdS);
						IdEnViejoSistema.getAmbitos().add(count);
						R.getDescription().add(IdEnViejoSistema);
						
						CompleteOperationalValue Valor=new CompleteOperationalValue(VisibleIDname,Boolean.toString(visiblebol));

						IdEnViejoSistema.getShows().add(Valor);
						}
						
						if (!typoString.isEmpty()) {
							CompleteTextElement TypoRecurso = new CompleteTextElement(this.TypoString,typoString);
							TypoRecurso.getAmbitos().add(count);
							R.getDescription().add(TypoRecurso);
							CompleteOperationalValue Valor=new CompleteOperationalValue(VisibleTypoString,Boolean.toString(visiblebol));
							TypoRecurso.getShows().add(Valor);
						}
						
						if (!typoRecString.isEmpty()) {
							CompleteTextElement TypoRecurso2 = new CompleteTextElement(this.TypoRecString,typoRecString);
							TypoRecurso2.getAmbitos().add(count);
							R.getDescription().add(TypoRecurso2);
							CompleteOperationalValue Valor=new CompleteOperationalValue(VisibleTypoRecString,Boolean.toString(visiblebol));
							TypoRecurso2.getShows().add(Valor);
						}
						

						}
					else System.err.println(CATEGORIAS_VACIAS);

				}
			rs.close();
			metaPadre.setAmbitoSTotales(MaxCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @return the attributo
	 */
	public CompleteLinkElementType getAttributo() {
		return Attributo;
	}

	/**
	 * @param attributo the attributo to set
	 */
	public void setAttributo(CompleteLinkElementType attributo) {
		Attributo = attributo;
	}

	
	
}
