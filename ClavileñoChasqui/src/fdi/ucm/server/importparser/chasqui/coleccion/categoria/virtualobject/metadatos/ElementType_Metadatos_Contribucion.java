package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteStructure;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea la contribucion
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Metadatos_Contribucion implements InterfaceChasquiparser{



	private CompleteElementType atributosMetadatosContribucion;
	
	private CompleteTextElementType AMFecha;
	private CompleteTextElementType AMAutor;
	private CompleteTextElementType AMpapel;

	private CompleteIterator Father;

	private LoadCollectionChasqui LCole;


	/**
	 * Constructor de la clase por parametros
	 * @param name nombre del Meta
	 * @param browseable Navegabilidad
	 * @param father Padre del Meta.
	 * @param multi si es multievaluado
	 */
	public ElementType_Metadatos_Contribucion(
			String name, boolean browseable, CompleteStructure father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		Father = (CompleteIterator) father;
		atributosMetadatosContribucion=new CompleteElementType(name, Father);
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		atributosMetadatosContribucion.getShows().add(VistaOV);
	}

	
	
	@Override
	public void ProcessAttributes() {

		ElementType_Metadatos_Contribucion_Autor localAuto = new ElementType_Metadatos_Contribucion_Autor("Autor", false, atributosMetadatosContribucion,LCole);
		localAuto.ProcessAttributes();
		AMAutor=localAuto.getExtendMetaControlled();
		atributosMetadatosContribucion.getSons().add(AMAutor);
		ElementType_Metadatos_Contribucion_Papel local = new ElementType_Metadatos_Contribucion_Papel("Papel", false, atributosMetadatosContribucion,LCole);
		local.ProcessAttributes();
		AMpapel=local.getExtendMetaControlled();
		atributosMetadatosContribucion.getSons().add(AMpapel);
		{
			AMFecha=new CompleteTextElementType("Fecha", atributosMetadatosContribucion);
			atributosMetadatosContribucion.getSons().add(AMFecha);
			
			CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			VistaOV.getValues().add(Valor);
			VistaOV.getValues().add(Valor2);
			VistaOV.getValues().add(Valor3);
			AMFecha.getShows().add(VistaOV);
			
			CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
			CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.DATE,VistaMetaType);
			VistaMetaType.getValues().add(MetaType);
			AMFecha.getShows().add(VistaMetaType);
			
		}
		}
	
	
	@Override
	public void ProcessInstances() {

		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct idov,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/contribute/role/source/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				Integer preIdov=null;
				int MaxCount=0;
				int count = 1;
				while (rs.next()) {
					
					
					String idov=rs.getObject("idov").toString();
					String num_ruta=rs.getObject("num_ruta").toString();
					if (idov!=null&&!idov.isEmpty())
						{
						Integer idovInt=Integer.parseInt(idov);
						
						if (preIdov!=null&&preIdov.intValue()==idovInt.intValue())
							count++;
						else
							{
							if (count>MaxCount)
								MaxCount=count;
							preIdov=idovInt;
							count=0;
							}
						procesaContributor(idov,num_ruta,this,count);
						}
					
				}
			rs.close();
			Father.setAmbitoSTotales(MaxCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * Funcion que procesa un contribuidor para un idov dado
	 * @param idov idov a procesar
	 * @param num_ruta ruta a procesar
	 * @param context contexto al que pertenecen
	 * @param meta padre a insertar elementocos contribuccion.
	 */
	private void procesaContributor(String idov, String num_ruta, ElementType_Metadatos_Contribucion padre, int context) {
		
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM metadatos WHERE ("+
					"ruta='/manifest/metadata/lom/lifecycle/contribute/role/value/langstring' OR "+
					"ruta='/manifest/metadata/lom/lifecycle/contribute/centity/vcard' OR "+
					"ruta='/manifest/metadata/lom/lifecycle/contribute/date/datetime') AND idov="+idov );
			if (rs!=null) 
			{
				while (rs.next()) {
					String contenido=rs.getObject("contenido").toString();
					String num_rutaValid=rs.getObject("num_ruta").toString();
					String ruta=rs.getObject("ruta").toString();
					String[] DatosRutaSection=num_ruta.split("\\.");
					String[] DatosRutaEntry=num_rutaValid.split("\\.");
					if (contenido!=null&&num_rutaValid!=null&&!num_rutaValid.isEmpty()&&!contenido.isEmpty()&&DatosRutaSection[4].equals(DatosRutaEntry[4]))
						{
						Integer idovInt=Integer.parseInt(idov);
						if (ruta.equals("/manifest/metadata/lom/lifecycle/contribute/centity/vcard"))
							{
							CompleteTextElement EMTV=new CompleteTextElement(padre.getAMAutor(),contenido);
							EMTV.getAmbitos().add(context);
							CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(idovInt);
							OV.getDescription().add(EMTV);
							}
						if (ruta.equals("/manifest/metadata/lom/lifecycle/contribute/role/value/langstring"))
							{
							CompleteTextElement EMTV=new CompleteTextElement(padre.getAMpapel(),contenido);
							EMTV.getAmbitos().add(context);
							CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(idovInt);
							OV.getDescription().add(EMTV);
							}
						if (ruta.equals("/manifest/metadata/lom/lifecycle/contribute/date/datetime"))
							{
							 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							if (!contenido.isEmpty())
								{
								Date D= formatter.parse(contenido);
								DateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
								String valueE=df.format(D);
								CompleteTextElement EMTV=new CompleteTextElement(padre.getAMFecha(), valueE);
								EMTV.getAmbitos().add(context);
								CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(idovInt);
								OV.getDescription().add(EMTV);
							}
							}
						}
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("Fecha incorrecta");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @return the ExtendMeta
	 */
	public CompleteElementType getExtendMeta() {
		return atributosMetadatosContribucion;
	}



	/**
	 * @return the aMFecha
	 */
	public CompleteTextElementType getAMFecha() {
		return AMFecha;
	}



	/**
	 * @param aMFecha the aMFecha to set
	 */
	public void setAMFecha(CompleteTextElementType aMFecha) {
		AMFecha = aMFecha;
	}



	/**
	 * @return the aMAutor
	 */
	public CompleteTextElementType getAMAutor() {
		return AMAutor;
	}



	/**
	 * @param aMAutor the aMAutor to set
	 */
	public void setAMAutor(CompleteTextElementType aMAutor) {
		AMAutor = aMAutor;
	}



	/**
	 * @return the aMpapel
	 */
	public CompleteTextElementType getAMpapel() {
		return AMpapel;
	}



	/**
	 * @param aMpapel the aMpapel to set
	 */
	public void setAMpapel(CompleteTextElementType aMpapel) {
		AMpapel = aMpapel;
	}
	
	
	

}
