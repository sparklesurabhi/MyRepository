prod
// UPDATE PRICING -- ADVANCED MODIFICATION //
// Variables //
default=defaultscript;  //This is the variable that you change if this function is used for a default line function instead of a update line function
res = "";
params = "";
discount=0.0;
discount2=0.0;
extendedDiscount=0.0;
discountAmt=0.0;
discountPer=0.0;
extendedCost=0.0;
costEa=0.0;
extendedList=0.0;
surchargePercent=0.0; 
surchargeAmt=0.0;
netPriceEach=0.0;
extNet=0.0;
netMarginAmtEach=0.0;
netMarginAmt=0.0;
sellDiscount2=0.0;
sellDiscount=0.0;
sellDiscountAmt=0.0;
sellDiscountPer=0.0;
sellExtendedDiscount=0.0;
sellPriceEa=0.0;
extendedSellPrice=0.0;
grossProfitEach=0.0;
grossProfit=0.0;
grossMarginPer=0.0;
BLMlist = 0.0;


//Support and Warranty Information
returnSb = stringbuilder();
count="";
numberOfGuages=0;
powerhub=0;
otconfig=0;
gcpconfig=0;
gciconfig=0;
uioptions=0;
vision=0;
temperature=0;
str= stringbuilder();
jsonObj1= json();
quantity = 1;
rootLineNumDict = dict("string");
qtyDict = dict("integer");
defineQuoteType="";
totalComponentPrice=0;

//price roolup information
modelListPriceDict = dict("float");
distributorListPrice = dict("float");
extDiscount = dict("float");

//Header level Var
totalCost=0.0;  if(default==true){totalCost=totalCost_quote;}
totalListPrice=0.0;  if(default==true){totalListPrice=totalListPrice_quote;}
totalLineItemDiscounts=0.0;  if(default==true){totalLineItemDiscounts=totalLineItemDiscounts_quote;}
totalSurcharge=0.0;  if(default==true){totalSurcharge=totalSurcharge_quote;}
totalNetPrice=0.0;  if(default==true){totalNetPrice=totalNetPrice_quote;}
totalLineItemSellDiscount=0.0;  if(default==true){totalLineItemSellDiscount=totalLineItemSellDiscount_quote;}  
sellPriceSubtotal=0.0;  if(default==true){sellPriceSubtotal=sellPriceSubtotal_quote;}  
subtotalPreQuoteDiscounts=0.0;
bottomLineDiscount2=0.0;
totalDiscount=0.0;
subtotalPostQuoteDiscounts=0.0;
total=0.0;
totalMarginPerc=0.0;
totalMarginDollar=0.0;
sellPriceSubtotalPostMiscItems=0.0;
distributorBottomLineDiscount2=0.0;
subtotalPostSellDiscount=0.0;
totalSellPrice=0.0;
totalGrossProfitPerc=0.0;
totalGrossProfitDollar=0.0;
bottomLinediscountAmt=0.0;
bottomLineDiscountPer=0.0;
distributorBottomLinediscountAmt=0.0;
distributorBottomLineDiscountPer=0.0;
totalMiscItemsDistributors=0.0;
longestLeadTime = 0; //used to store the maximum lead time of all line items - ZT SFDC Case 11299
leadtime_line="";

//Added by Kevin Deichl (Approval Discount Calculation)
realTotalListPrice = 0.0;
totalListPriceWithoutCOMM = 0.0;
realExtendedList = 0.0;
overridenListPriceDiscounts = 0.0;
discountTotalLineItemDiscounts = 0.0;
discountWithoutCOMMPart = 0.0;
//Added for Optional line
optionalLine_Price=0.0;
withoutOptionalLine_Price=0.0; 

//Set the discount2 type symbols so they can be used in the to concantenate them to the discount 2 type when returning values
discount2Type=" ";
sellDiscount2Type=" "; 
bottomLineDiscount2Type=" "+_system_current_document_currency_pref; 
	if(bottomLineDiscountType_quote=="Amt"){bottomLineDiscount2Type=" %";}
distributorBottomLineDiscount2Type=" "+_system_current_document_currency_pref; 
	if(distributorBottomLineDiscountType_quote=="Amt"){distributorBottomLineDiscount2Type=" %";}
 
discountDict = dict("float");
amtDiscountDict = dict("float");
parentQtyDict = dict("integer");
count = "1";
for line in line_process{
	lineNum = line._document_number;
    parLineNum = line._parent_doc_number;
     put(discountDict,lineNum,line.discount_line);
    if(line.discountType_line == "Amt"){
		if(line._part_number == "" AND line._parent_doc_number == "" AND line.bLMlist_line <> 0){
			extListPrice = (line.discount_line *100 )/line.bLMlist_line;
			put(amtDiscountDict,lineNum,extListPrice);
			put(parentQtyDict,lineNum,line._price_quantity);
		}
     put(discountDict,lineNum,0.0);
    }
   
	 if (parLineNum == "") {
        put(rootLineNumDict, lineNum, lineNum);
    } else {
        dictKeys = keys(rootLineNumDict);
        if (findinarray(dictKeys, parLineNum) > -1) {
            put(rootLineNumDict, lineNum, get(rootLineNumDict, parLineNum));
        }
    }
	put(qtyDict,lineNum,line._price_quantity);
	rootNumber = get(rootLineNumDict,parLineNum);
	quantity = get(qtyDict,lineNum);
	if(line._parent_doc_number=="")
	{
	
	   count=getconfigattrvalue(line._document_number, "warrantyAndRemote_numberOfGuages");
	   if(count <> "" and isnumber(count))
	   {
			numberOfGuages=(atoi(count) * quantity)+numberOfGuages;
	   }
	   
	   
	}
	pricebook=_quote_process_price_book_name;
	if(find (pricebook, "SERIES-9-") >= 0)
	{
	
	if(line._model_variable_name=="sERIES9SENSORATO")
	{
		powdervision=getconfigattrvalue(line._document_number, "sensor_beamPatchSensor");
		  
		if(powdervision <> "" AND  NOT(isnull(powdervision)) AND powdervision=="Series 9 gauge for PowerVision")
		{	        
			vision=vision+atoi(count) * quantity;
				  
		}
	 	 
		coolingoption=getconfigattrvalue(line._document_number, "sensor_sensorCoolingOption");
		if(coolingoption <> "" AND  NOT(isnull(coolingoption))){	        
			temperature=temperature+atoi(count) * quantity;    
         }
		
		heatingoption=getconfigattrvalue(line._document_number, "sensor_sensorHeatingOption");
		if(heatingoption <> "" AND  NOT(isnull(heatingoption))){	        
			temperature=temperature+atoi(count) * quantity;    
         }		
		 
	}
	
	if(line._model_variable_name=="sERIES9UIATO")
	   {
		
		sehQty = 1;
		if(isnumber(getconfigattrvalue(line._document_number, "Ui_sEHQuantity"))){
			sehQty = atoi(getconfigattrvalue(line._document_number, "Ui_sEHQuantity"));
		}
		enclosureOptions=getconfigattrvalue(line._document_number, "UI_enclosureOptions");
		if(enclosureOptions <> "" AND  NOT(isnull(enclosureOptions))){
			powerhub=powerhub+quantity*sehQty;
	    }
		
		
		oTQty = 1;
		if(isnumber(getconfigattrvalue(line._document_number, "ui_oTQuantity"))){
			oTQty = atoi(getconfigattrvalue(line._document_number, "ui_oTQuantity"));
		}
		otconfiguration=getconfigattrvalue(line._document_number, "UI_oTConfiguration");
	    if(otconfiguration <> "" AND  NOT(isnull(otconfiguration))){
			otconfig=otconfig+quantity*oTQty;
	    }
		
		
		gcpQty = 1;
		if(isnumber(getconfigattrvalue(line._document_number, "Ui_gCPQuantity"))){
			gcpQty = atoi(getconfigattrvalue(line._document_number, "Ui_gCPQuantity"));
		}
	    gCPConfiguration=getconfigattrvalue(line._document_number, "UI_gCPConfiguration");
	    if(gCPConfiguration <> "" AND  NOT(isnull(gCPConfiguration))){
			gcpconfig=gcpconfig+quantity*gcpQty;
	    }
		
		gCIConfiguration=getconfigattrvalue(line._document_number, "UI_gCIConfiguration");
		gciQty = 1;
		if(isnumber(getconfigattrvalue(line._document_number, "UI_gCIQuantity"))){
			gciQty = atoi(getconfigattrvalue(line._document_number, "UI_gCIQuantity"));
		}
	    if(gCIConfiguration <> "" AND  NOT(isnull(gCIConfiguration))){
			gciconfig=gciconfig+quantity*gciQty ;
	    }
		
		options=getconfigattrvalue(line._document_number, "UI_options");
		arroptions=split(options, ",");
		for i in arroptions {
		   if(options <> "" AND  NOT(isnull(options)) AND i=="Handheld GI (RJ45 or wireless)"){
			  uioptions=uioptions+quantity;
			  }
			  
		   }
	   }
	
	}elif(find (pricebook, "PREDIKTIR II-") >= 0)
	{
		
	    
	if(line._model_variable_name=="pREDIKTIRIISENSORATO")
	{
	  powdervision=getconfigattrvalue(line._document_number, "PrediktIRpowderVision");
	  
	if(powdervision <> "" AND  NOT(isnull(powdervision)) AND powdervision=="true")
	{	        
	    vision=vision+quantity;
		      
         }
	}
	if(line._model_variable_name=="pREDIKTIRIIUIATO")
	{
		gCIConfiguration=getconfigattrvalue(line._document_number, "PrediktIRgCIOptions");
		if(gCIConfiguration <> "" AND  NOT(isnull(gCIConfiguration))){
			gciconfig=gciconfig+quantity;
	    }
		  
		enclosureOptions=getconfigattrvalue(line._document_number, "enclosureOptions");
	    if(enclosureOptions <> "" AND  NOT(isnull(enclosureOptions))){
			powerhub=powerhub+quantity;
	    }	
	}	
	}
}
print amtDiscountDict;
jsonput(jsonObj1, "numberOfGuages", numberOfGuages);
jsonput(jsonObj1, "sensor_powderVision", vision);
jsonput(jsonObj1, "sensor_temperature", temperature);
jsonput(jsonObj1, "UI_enclosureOptions", powerhub);
jsonput(jsonObj1, "UI_oTConfiguration", otconfig);
jsonput(jsonObj1, "UI_gCPConfiguration", gcpconfig);
jsonput(jsonObj1, "UI_gCIConfiguration", gciconfig);
jsonput(jsonObj1, "UI_options", uioptions);
sbappend(returnSb,"1~supportAndWarrantyOptions_FBT_quote~",jsontostr(jsonObj1),"|"); 
additionalItemsModelListPrice = 0;
additionalItemsDistributorPrice = 0;
isFirstBundleFound = false;
firstBundleDocNumber = "-1";
extendedDiscountDict = dict("float");
extendedNetDict = dict("float");
convRate = 1;
resultSet = bmql("select ToCurrency, Rate from FBnTConversionRates where ToCurrency = $quotingCurrency_quote ");
for each in resultSet{
		convRate = atof(get(each, "Rate"));
}
// loop through line items
for line in line_process {
	if(NOT isFirstBundleFound AND line._part_number == "" AND line._parent_doc_number==""){
		firstBundleDocNumber = line._document_number;
		isFirstBundleFound = true;
	}
	
    if (line.overrideSellPrice_line==true) {
	    BLMlist = line.writeinListPrice_line;
       overridenListPriceDiscounts = overridenListPriceDiscounts + line._price_list_price_each - line.writeinListPrice_line; //Added by Kevin Deichl (Approval Discount Calculation)
	   if(Type_FBT_quote== "Distributor"){
	   sbappend(returnSb,line._document_number,"~originalListPrice_FBT_l~"+string(line.writeinListPrice_line),"|");
	   }
    } elif((Type_FBT_quote == "Distributor" OR line._part_number == "") AND marketCode=="Food & Bulk"){ // Added NDC logic
	    BLMlist=line.bLMlist_line;
    }else
	{
		BLMlist = line._price_list_price_each;
	}
	if(line._part_number == "FREIGHT"){
		BLMlist = shippingCosts_FBT_quote;
		if(Type_FBT_quote== "Distributor"){
			sbappend(returnSb,line._document_number,"~originalListPrice_FBT_l~"+string(shippingCosts_FBT_quote),"|");
		}
	}
	if(line._part_number == "COMM" AND (regionCode_FBT_quote == "C00 -- China -- General" OR regionCode_FBT_quote == "E11 India")){
		BLMlist = cOMMPrice_FBT_quote;
		if(Type_FBT_quote== "Distributor"){
			sbappend(returnSb,line._document_number,"~originalListPrice_FBT_l~"+string(cOMMPrice_FBT_quote),"|");
		}
	}
	if(line._model_variable_name=="warrantyAndRemote"){
			
		defineQuoteType=getconfigattrvalue(line._document_number,"defineQuoteType");
		configuredGauge=getconfigattrvalue(line._document_number,"configuredGauge");
		recordSet = bmql("select HardwareComponent,Price from WarrantyPrices");
		priceDict = dict("float");
		for each in recordSet{
			component = get(each,"HardwareComponent");
			price = atof(get(each,"Price"));
			put(priceDict,component,price);
		}
		if(configuredGauge=="Series 9"){
			totalPricePerYear = get(priceDict,"Gauge") * (numberOfGuages) + get(priceDict,"OT") * (otconfig) + get(priceDict,"CGI") * (gciconfig) + get(priceDict,"GCP") * (gcpconfig) + get(priceDict,"Remote") * (uioptions) + get(priceDict,"PH") * (powerhub);
			totalAdjustmentPrice = get(priceDict,"Series_Gauage") * (numberOfGuages) + get(priceDict,"Series_PowerHub") * (powerhub) +  get(priceDict,"Series_GCI") * (gciconfig) + get(priceDict,"Series_GCP") * (gcpconfig) + get(priceDict,"Series_GI") * (uioptions) + get(priceDict,"Series_PV") * (vision) + get(priceDict,"Series_Temp") * (temperature);
			adjustedListPrice = 0.01 * totalAdjustmentPrice;
			currencyConversion = 1 ;
			warrantyExtension=getconfigattrvalue(line._document_number, "selectNumberOfYearsWarrantyExtension");
			totalComponentPrice = (adjustedListPrice + totalPricePerYear * currencyConversion) * atof(warrantyExtension);
			totalComponentPrice=round(totalComponentPrice*convRate,0);
		}
		elif(configuredGauge=="PrediktIR II"){
			totalPricePerYear = get(priceDict,"Prediktir_Guage") * (numberOfGuages) + get(priceDict,"Prediktir_OT") * (otconfig) + get(priceDict,"Prediktir_CGI") * (gciconfig) + get(priceDict,"Prediktir_PH") * (powerhub) + + get(priceDict,"Prediktir_PV") * (vision);
			currencyConversion = 1 ;
			warrantyExtension=getconfigattrvalue(line._document_number, "selectNumberOfYearsWarrantyExtension");
			totalComponentPrice = (totalPricePerYear * currencyConversion) * atof(warrantyExtension);
			totalComponentPrice=round(totalComponentPrice*convRate,0); 
		}
	}
	if(line._line_item_comment == defineQuoteType AND defineQuoteType <> "" AND line._line_item_comment <> "")
	{
		sbappend(returnSb,line._document_number,"~bLMlist_line~"+string(totalComponentPrice),"|");
		sbappend(returnSb,line._document_number,"~originalListPrice_FBT_l~"+string(totalComponentPrice),"|");
		BLMlist = totalComponentPrice;
	}
	if(line._parent_line_item=="Extended Warranty and Support" AND Type_FBT_quote== "Distributor") 
	{
	  discount = extededWarrantyDistiDisc_quote;
	  totalprice=totalComponentPrice-(totalComponentPrice*(discount/100));
	  sbappend(returnSb,line._document_number+"~bLMlist_line~",string(totalprice),"|");
	  BLMlist = totalprice;
	  
	}
	if(line._line_item_comment == "VPN_Remote_Warranty" OR line._line_item_comment == "VPN_Warranty"){
		remoteType = line._line_item_comment;
		recordSet = bmql("select Price from WarrantyPrices where HardwareComponent = $remoteType");
		priceVal ="";
		for each in recordSet{
		        roundedPrice = round( convRate * atof(get(each,"Price")),0);
			priceVal  = string( roundedPrice );
			//priceVal  = string( convRate * atof(get(each,"Price")));
			
		}
		sbappend(returnSb,line._document_number,"~bLMlist_line~",priceVal,"|");
		sbappend(returnSb,line._document_number,"~originalListPrice_FBT_l~",priceVal,"|");
		BLMlist = atof(priceVal);
		if(line._parent_line_item=="Extended Warranty and Support" AND Type_FBT_quote== "Distributor") 
		{
	  		discount = extededWarrantyDistiDisc_quote;
	  		totalprice=atof(priceVal)-(atof(priceVal)*(discount/100));
	  		sbappend(returnSb,line._document_number+"~bLMlist_line~",string(totalprice),"|");
	  		BLMlist = totalprice;
	  
	}
	}
	if(line._parent_line_item<>"" AND line.distiDiscount_FBT_l <> "" AND  Type_FBT_quote== "Distributor") 
	{
	  discount = atof(line.distiDiscount_FBT_l);
	  totalprice=line._price_unit_price_each-(line._price_unit_price_each*(discount /100));
	  sbappend(returnSb,line._document_number+"~bLMlist_line~",string(totalprice),"|");
	  BLMlist = totalprice;
	  
	}
	modelListPrice = BLMlist * line._price_quantity;
	if(line.optional_line){
		modelListPrice  = 0.0;
	}
    distributorPrice = 0;
    if(isnumber(line.originalListPrice_FBT_l)){
    	distributorPrice  = atof(line.originalListPrice_FBT_l);
    }  
	rootNumber = get(rootLineNumDict,line._parent_doc_number);
	if(containskey(modelListPriceDict,rootNumber))
	{	
		put(modelListPriceDict, rootNumber, get(modelListPriceDict, rootNumber) + modelListPrice);	
		if(Type_FBT_quote == "Distributor"){
			put(distributorListPrice , rootNumber, get(distributorListPrice , rootNumber) + distributorPrice*line._price_quantity  );			
		}
	}	  
	elif(rootNumber <>"" AND line._parent_doc_number <> "")
	{	
		put(modelListPriceDict, rootNumber, modelListPrice);	
		if(Type_FBT_quote == "Distributor"){
			put(distributorListPrice,rootNumber,distributorPrice*line._price_quantity );	   
		}           
	}
	if(line._part_number <> "" AND line._parent_doc_number == ""){
		additionalItemsModelListPrice = additionalItemsModelListPrice + modelListPrice;
		additionalItemsDistributorPrice = additionalItemsDistributorPrice + distributorPrice;
	}
	//Initial Lead Time from Part master
	if(default==true) {
		if(line._part_lead_time == "N/A" or line._part_lead_time == "") {
			res = res + line._document_number + "~partLeadTimeDays_line~0|";	
		} else {
			res = res + line._document_number + "~partLeadTimeDays_line~" + line._part_lead_time + "|";	
		}
		res = res + line._document_number + "~oldQuoteFlag~x|";
		if(isnumber(line._part_lead_time)){
			if(atoi(line._part_lead_time) > longestLeadTime){//if this line item has a longer lead time than the longest so far
				longestLeadTime = atoi(line._part_lead_time);
			}
		}
	}

	if(line.partLeadTimeDays_line == 0) {
		res = res + line._document_number + "~partLeadTimeDays_line~0|";
		res = res + line._document_number + "~oldQuoteFlag~x|";
	}

	if(line.oldQuoteFlag == "") {
	  if(line._part_lead_time == "N/A" or line._part_lead_time == "") {
		res = res + line._document_number + "~partLeadTimeDays_line~0|";
	  } else {
		res = res + line._document_number + "~partLeadTimeDays_line~" + line._part_lead_time + "|";
	  }
	  
	  if(isnumber(line._part_lead_time)){
		if(atoi(line._part_lead_time) > longestLeadTime){//if this line item has a longer lead time than the longest so far
			longestLeadTime = atoi(line._part_lead_time);
		}
	  }
	  res = res + line._document_number + "~oldQuoteFlag~x|";
	}

    //ZT SFDC Case 11299
	if(line.partLeadTimeDays_line > longestLeadTime){//if this line item has a longer lead time than the longest so far
	    longestLeadTime = line.partLeadTimeDays_line;
	}



    //Set the discount2 type symbols so they can be used in the to concantenate them to the discount 2 type when returning values
    discount2Type=" "+_system_current_document_currency_pref; 
	    if(line.discountType_line=="Amt"){discount2Type=" %";}
    sellDiscount2Type=" "+_system_current_document_currency_pref; 
	    if(line.sellDiscountType_line=="Amt"){sellDiscount2Type=" %";}

	// Calculate Cost values.  NOTE: costEa value will have to be changed based on the part field that it is stored in //
//    costEa=line.costEa_line;
//    if (default==true and isnumber(line._part_custom_field1) and line.optional_line<>true){costEa=round(atof(line._part_custom_field1),2);}
    // Calculate Cost values.  NOTE: costEa value will have to be changed based on the part field that it is stored in //
    if(find(line._part_number,"SPECIAL") <> -1 OR startswith(line._part_number, "AM")){
		if(line.cost_l == 0){
		costEa=0.0;
		}
		else{
		if(quotingCurrency_quote=="EUR"){
		costEa=euroConversionFactor*line.cost_l;
		}
		elif(quotingCurrency_quote=="GBP"){
		costEa=gBPConversionFactor*line.cost_l;
		}
		else {
		costEa=line.cost_l;
		}
	   
		if (default==true and line.optional_line<>true){costEa=round(line.cost_l,2);}
		if (default==true and line.optional_line<>true and quotingCurrency_quote=="EUR"){costEa=round(euroConversionFactor*line.cost_l,2);}
		if (default==true and line.optional_line<>true and quotingCurrency_quote=="GBP"){costEa=round(gBPConversionFactor*line.cost_l,2);}
		}
	}else{
		if(line._part_custom_field1==""){
		costEa=0.0;
		}
		else{
		if(quotingCurrency_quote=="EUR"){
		costEa=euroConversionFactor*atof(line._part_custom_field1);
		}
		elif(quotingCurrency_quote=="GBP"){
		costEa=gBPConversionFactor*atof(line._part_custom_field1);
		}
		else {
		costEa=atof(line._part_custom_field1);
		}
	   
		if (default==true and isnumber(line._part_custom_field1) and line.optional_line<>true){costEa=round(atof(line._part_custom_field1),2);}
		if (default==true and isnumber(line._part_custom_field1) and line.optional_line<>true and quotingCurrency_quote=="EUR"){costEa=round(euroConversionFactor*atof(line._part_custom_field1),2);}
		if (default==true and isnumber(line._part_custom_field1) and line.optional_line<>true and quotingCurrency_quote=="GBP"){costEa=round(gBPConversionFactor*atof(line._part_custom_field1),2);}
		}

	}
	fBNTPartTotalCost  = 0;
	if(isnumber(line._part_custom_field1) AND atof(line._part_custom_field1) > 0){
		partCost = atof(line._part_custom_field1);
		fBNTPartCost = partCost * convRate;
		overheadCost = fBNTPartCost * line.overhead_FBT_l /100;
		fBNTPartTotalCost = round(fBNTPartCost + overheadCost , 2);	
		res = res + line._document_number +"~fBNTPartCost_FBT_l~"+string(fBNTPartCost) + "|";
		res = res +line._document_number+"~totalCost_FBT_l~"+string(fBNTPartTotalCost) + "|";
	}
    extendedCost=fBNTPartTotalCost  *line._price_quantity;
	if (line.optional_line<>true AND line.filterWheelPart_FBT_l <> "Y"){
	totalCost=totalCost+extendedCost;
		}
    // Calculate Extended list
    //extendedList=line._price_quantity*line._price_list_price_each;
	extendedList=line._price_quantity*BLMlist;
      realExtendedList = line._price_quantity*line._price_list_price_each;//Added by Kevin Deichl (Approval Discount Calculation)
	//Added for gaging solution
	if(line._part_number == ""){
		 realExtendedList = line._price_quantity*BLMlist;
	}
//if price override is used then use sell price instead of list price
	//if (line.overrideSellPrice_line == true){
	    //extendedList=line._price_quantity*line.sellPriceEa_line;
	//}
	if (line.optional_line<>true AND (line._part_number <> "" AND line._parent_doc_number == "" )){
	    totalListPrice=totalListPrice+extendedList;
       //realTotalListPrice=realTotalListPrice+realExtendedList; //Added by Kevin Deichl (Approval Discount Calculation)
	}
   
   //Moved from 2 lines above, by Kevin Deichl on 2/10/12
   if(line._part_number <>"" AND line._parent_doc_number == "" AND line.optional_line <> true){
   	realTotalListPrice=realTotalListPrice+realExtendedList; //Added by Kevin Deichl (Approval Discount Calculation)
		if(line._part_number <> "COMM"){
			totalListPriceWithoutCOMM = totalListPriceWithoutCOMM + realExtendedList;
		}
	}
	discount = line.discount_line; //CH 100109
	if(line._parent_doc_number <> "" AND line._part_number <> ""){
		rootNumber = get(rootLineNumDict,line._document_number);
		discount = get(discountDict,rootNumber);
	}
	//#Calculate the Discount2 & Ext Discount values.  If user selects/enters "%" it should calculate $, if user enters amt, it will calculate %
	
 
    if (line.discountType_line=="%") {
		    discount2 = round(BLMlist*(discount/100),2);
		    discountAmt = discount2; 
		    discountPer = discount;

	  } else { //discountType_line == "Amt"
	    if (BLMlist<>0){
	        //discount2=round((line.discount_line/line._price_list_price_each)*100,2); //ERROR
			    discount2=discount*line._price_quantity;
	    }
		  discountAmt = discount; 
      discountPer = discount2;


	}
	extendedDiscount = discountAmt * line._price_quantity;
	print extendedDiscount ;
	put(extDiscount,line._document_number,extendedDiscount );


	if (line.optional_line<>true AND (line._part_number <> "" AND line._parent_doc_number == "")){
		totalLineItemDiscounts=totalLineItemDiscounts+extendedDiscount;
	}
	if(line._part_number <> "" AND line._parent_doc_number == "" AND line.optional_line <> true){
   		discountTotalLineItemDiscounts = discountTotalLineItemDiscounts + extendedDiscount; //Added by Kevin Deichl - 2/10/12
		if(line._part_number <> "COMM"){
			
			discountWithoutCOMMPart = discountWithoutCOMMPart + extendedDiscount;
		}
	}
	
	//Calculate Surcharges
	surchargePercent=line.surchargePercent_line; //Note this is where you would look this value up in a table if it is applicable or hardcode the number in

	//surchargeAmt=(surchargePercent/100)*line._price_list_price_each;
	surchargeAmt=(surchargePercent/100)*BLMlist; //CH 092809

	//not using surcharges, but adding this line for completeness
	if (line.overrideSellPrice_line == true){
		surchargeAmt=(surchargePercent/100)*line.sellPriceEa_line; 
	}

	if (line.optional_line<>true){
		totalSurcharge=totalSurcharge+(surchargeAmt*line._price_quantity);
	}

  	//# Calculate Net Price //
	  netPriceEach = BLMlist - discountAmt + surchargeAmt; //CH 092809
    
    
    extNet = netPriceEach * line._price_quantity;
	
	if(line.discountType_line == "Amt"){
		put(extendedDiscountDict,line._document_number,discountAmt );
		put(extendedNetDict,line._document_number,netPriceEach );
	}else{
		if(line.optional_line <> true){
			if(containskey(extendedDiscountDict,rootNumber)){
				put(extendedDiscountDict,rootNumber,get(extendedDiscountDict,rootNumber)+extendedDiscount);
				put(extendedNetDict,rootNumber,get(extendedNetDict,rootNumber)+extNet);
			}else{
				put(extendedDiscountDict,rootNumber,extendedDiscount);
				put(extendedNetDict,rootNumber,extNet);
			}
		}
	}
   	if (line.optional_line<>true  AND (line._parent_doc_number == "" AND line._part_number <> "")){
  	    totalNetPrice=totalNetPrice+extnet;
  	}

   //#calculate Price for Option line
	 if (line.optional_line == true ){
  	    optionalLine_Price=optionalLine_Price+extNet;
  	   }
	
   //#calculate Price without Option line
	if (line.optional_line <> true  AND line._model_variable_name == "" )
	{
  	    withoutOptionalLine_Price=withoutOptionalLine_Price+extNet;
  	}


    //Calculate Margins
	netMarginAmtEach=netPriceEach-costEa;
	netMarginAmt=netMarginAmtEach*line._price_quantity;
	if (netPriceEach<>0){netMarginPercent=((netPriceEach-costEa)/netPriceEach) *100;}
	else { netMarginPercent = 0.0;}
	
	//#Calculate the Sell discounts.  Note: this only applies when there is a distributor network
	//#Calculate the Sell Discount2 & Ext Sell Discount2 values.  If user selects/enters "%" it should calculate $, if user enters amt, it will calculate %.  If the user selects markup the %,$ and the sellDiscount2 don't apply.

    if(line.sellDiscountType_line=="%"){
	    if(line.overrideSellPrice_line==false){
		    sellDiscount=line.sellDiscount_line;
	        //sellDiscount2=line._price_list_price_each*(line.sellDiscount_line/100);
			sellDiscount2=BLMlist*(line.sellDiscount_line/100); //CH 092809
		} else{
		    //sellDiscount2=line._price_list_price_each+surchargeAmt-line.sellPriceEa_line;
			sellDiscount2=BLMlist+surchargeAmt-line.sellPriceEa_line; //CH 092809
		    //if (line._price_list_price_each<>0){sellDiscount=(1-((line.sellPriceEa_line-surchargeAmt)/line._price_list_price_each))*100;}}
			if (BLMlist<>0){ //CH 092809
				sellDiscount=(1-((line.sellPriceEa_line-surchargeAmt)/BLMlist))*100;
			}
		}
		sellDiscountAmt=sellDiscount2; sellDiscountPer=sellDiscount;
	} elif (line.sellDiscountType_line=="Amt"){
	    if(line.overrideSellPrice_line==false){
			sellDiscount=line.sellDiscount_line;
	        //if (line._price_list_price_each<>0){sellDiscount2=round((line.sellDiscount_line/line._price_list_price_each)*100,2);}
			if (BLMlist<>0){ //CH 092809
			    sellDiscount2=round((line.sellDiscount_line/BLMlist)*100,2); //CH 092809
			}
		} else {
            sellDiscount=line._price_list_price_each+surchargeAmt-line.sellPriceEa_line;
			//if (line._price_list_price_each<>0){sellDiscount2=round((1-(line.sellPriceEa_line-surchargeAmt/line._price_list_price_each))*100,2);}}	
			if (BLMlist<>0){
				sellDiscount2=round((1-(line.sellPriceEa_line-surchargeAmt/line._price_list_price_each))*100,2);
			}
		}	
		sellDiscountAmt=sellDiscount; sellDiscountPer=round(sellDiscount2,2);
	}	
	sellExtendedDiscount=sellDiscountAmt*line._price_quantity;
	
	if (line.optional_line<>true){
		totalLineItemSellDiscount=totalLineItemSellDiscount+sellExtendedDiscount;
	}
	
	//Calculate Sell prices. Note: this only applies when there is a distributor network
	if (line.overrideSellPrice_line==true and default==false){sellPriceEa=line.sellPriceEa_line;}
    //elif(default==true){sellPriceEa=line._price_list_price_each+surchargeAmt;}
	elif (default==true) {sellPriceEa=BLMlist+surchargeAmt;} //CH 092809
	else {
		//sellPriceEa = line._price_list_price_each; 
		sellPriceEa = BLMlist; //CH 092809
		if(line.sellDiscountType_line=="Markup"){
			sellPriceEa=netPriceEach*line.sellDiscount_line;
		} else {
			//sellPriceEa=line._price_list_price_each-sellDiscountAmt+surchargeAmt;
			sellPriceEa=BLMlist-sellDiscountAmt+surchargeAmt; //CH 092809
		}
	}
	extendedSellPrice=sellPriceEa*line._price_quantity;
	
	if (line.optional_line<>true){
	sellPriceSubtotal=sellPriceSubtotal+extendedSellPrice;
	}
	
	grossProfitEach=sellPriceEa-netPriceEach;
	grossProfit=grossProfitEach*line._price_quantity;
	if (sellPriceEa<>0){grossMarginPer=((sellPriceEa-netPriceEach)/sellPriceEa)*100;}
	
	// Build Results String //
	
	discountPercent = 0;
	if(containskey(amtDiscountDict,rootNumber)){
		discountPercent = get(amtDiscountDict,rootNumber);
		parentQty = get(parentQtyDict,rootNumber);
		discountAmountValue = round(BLMlist*(discountPercent /100),2);
		netAmountValue = BLMlist - discountAmountValue;
		
		extAmountvalue = netAmountValue*line._price_quantity*parentQty;
		res = res + line._document_number + "~netPrice_FBT_l~" + string(netAmountValue) + "|";
		res = res + line._document_number + "~extNetPrice_FBT_l~" + string(extAmountvalue) + "|";
	}else{	
		parentQty = 1;
		if(line._document_number <> rootNumber AND containskey(qtyDict,rootNumber)){
			parentQty = get(qtyDict,rootNumber);
		}
		res = res + line._document_number + "~netPrice_FBT_l~" + string(netPriceEach) + "|";
		res = res + line._document_number + "~extNetPrice_FBT_l~" + string(netPriceEach*line._price_quantity*parentQty) + "|";
	}
	res = res + line._document_number + "~costEa_line~" + string(costEa) + "|";
	res = res + line._document_number + "~extendedCost_line~" + string(extendedCost) + "|";
	res = res + line._document_number + "~extendedList_line~" + string(extendedList) + "|";
	res = res + line._document_number + "~discount_line~" + string(discount) + "|";
	res = res + line._document_number + "~discount2_line~" + string(discount2)+ "|";
	res = res + line._document_number + "~extendedDiscount_line~" + string(extendedDiscount) + "|";
	res = res + line._document_number + "~surchargePercent_line~" + string(surchargePercent) + "|";
	res = res + line._document_number + "~surchargeAmt_line~" + string(surchargeAmt) + "|";
	res = res + line._document_number + "~netPriceEach_line~" + string(netPriceEach) + "|";
	res = res + line._document_number + "~netMarginAmt_line~" + string(netMarginAmt) + "|";
	res = res + line._document_number + "~netMarginAmtEach_line~" + string(netMarginAmtEach) + "|";
	res = res + line._document_number + "~netMarginPercent_line~" + string(netMarginPercent) + "|";
	res = res + line._document_number + "~sellPriceEa_line~" + string(sellPriceEa) + "|";
	res = res + line._document_number + "~bLMlist_line~" + string(BLMlist) + "|";
	res = res + line._document_number + "~sellDiscount_line~" + string(sellDiscount) + "|";
	res = res + line._document_number + "~sellDiscount2_line~" + string(sellDiscount2)+sellDiscount2Type + "|";
	res = res + line._document_number + "~sellExtendedDiscount_line~" + string(sellExtendedDiscount) + "|";
	res = res + line._document_number + "~extendedSellPrice_line~" + string(extendedSellPrice) + "|";
	res = res + line._document_number + "~grossProfitEach_line~" + string(grossProfitEach) + "|";
	res = res + line._document_number + "~grossProfit_line~" + string(grossProfit) + "|";
	res = res + line._document_number + "~grossMarginPer_line~" + string(grossMarginPer) + "|";
	res = res + line._document_number + "~extendedDiscount_line~" + string(extendedDiscount) + "|";
	res = res + line._document_number + "~extendedNet_line~" + string(extNet) + "|";
	
	
	
}  // end line item loop

listPriceDictKeysArray = keys(modelListPriceDict);
ret = "";


for each in listPriceDictKeysArray {
    docNum = each;
    modelListPriceDictprice = get(modelListPriceDict, each);
	distributorListPriceVal = 0;
	if (Type_FBT_quote == "Distributor") {
		distributorListPriceVal = get(distributorListPrice, each) ;
	}
    //Needs to revisit and updated
    modelQty = 0;
    toatlExtNetDictprice = 0;
    extDiscountValue = 0.0;
    if (containskey(qtyDict, docNum)) {
        modelQty = get(qtyDict, docNum);
        extDiscountValue = get(extDiscount, docNum);
    }
    toatlExtNetDictprice = (modelListPriceDictprice * get(qtyDict, each)) - extDiscountValue;
    // ret = ret + docNum + "~extendedNet_line~" + string(extNetDictprice) + "|";
    ret = ret + docNum + "~extendedNet_line~" + string(toatlExtNetDictprice) + "|";
    
    ret = ret + docNum + "~bLMlist_line~" + string(modelListPriceDictprice) + "|";
    if (Type_FBT_quote == "Distributor") {
        ret = ret + docNum + "~originalListPrice_FBT_l~" + string(distributorListPriceVal) + "|";
    }
    print extendedDiscountDict;
	if(containskey(extendedDiscountDict,docNum)){
		ret = ret + docNum + "~extendedDiscount_line~" + string(get(extendedDiscountDict,docNum) * get(qtyDict, each)) + "|";
		totalLineItemDiscounts=totalLineItemDiscounts+get(extendedDiscountDict,docNum)* get(qtyDict, each);
		discountWithoutCOMMPart = discountWithoutCOMMPart +get(extendedDiscountDict,docNum)* get(qtyDict, each);
		//ret = ret + docNum + "~extendedNet_line~" + string(get(extendedNetDict,docNum) * get(qtyDict, each)) + "|";
	}
	 totalNetPrice=totalNetPrice+toatlExtNetDictprice;
	 totalListPrice= totalListPrice+ (modelListPriceDictprice * get(qtyDict, each));
	 totalListPriceWithoutCOMM = totalListPriceWithoutCOMM+ (modelListPriceDictprice * get(qtyDict, each));
	 discountTotalLineItemDiscounts =  totalLineItemDiscounts;
	 
}
realTotalListPrice = totalListPrice ;

//ZT SFDC Case 11299
leadWeeks = Integer(ceil(longestLeadTime/7.0));

if(leadWeeks== 0 AND overrideDelivery_FBT_quote <> "0" AND isnumber(overrideDelivery_FBT_quote)){
	leadWeeks = atoi(overrideDelivery_FBT_quote);
}
res = res + _quote_process_document_number + "~delivery_quote~" + String(leadWeeks) + "|";

//Total Misc Charges.  Misc Charges are assumed to be entered at Net price for these calculations.
totalMiscCharges=(miscCharge1_quote*miscChargeQty1_quote)+(miscCharge2_quote*miscChargeQty2_quote) +(miscCharge3_quote*miscChargeQty3_quote)+(miscCharge4_quote*miscChargeQty4_quote)+(miscCharge5_quote*miscChargeQty5_quote)+(miscCharge6_quote*miscChargeQty6_quote);
subtotalPreQuoteDiscounts=totalNetPrice+totalMiscCharges;

//Bottom Line Discounts
    if(bottomLineDiscountType_quote=="%"){
	    bottomLineDiscount2=subtotalPreQuoteDiscounts*(bottomLineDiscount_quote/100);
		bottomLinediscountAmt=bottomLineDiscount2; bottomLineDiscountPer=bottomLineDiscount_quote;}
    else{
	    bottomLineDiscount2=(bottomLineDiscount_quote/subtotalPreQuoteDiscounts)*100;
		bottomLineDiscountAmt=bottomLineDiscount_quote; bottomLineDiscountPer=bottomLineDiscount2;}
		
//Total Discount		
	totalDiscount=bottomLineDiscountAmt+totalLineItemDiscounts;	
   //realTotalDiscount=totalDiscount+overridenListPriceDiscounts;

realTotalDiscount=bottomLineDiscountAmt+discountTotalLineItemDiscounts;
totalDiscountWithoutCOMM = bottomLineDiscountAmt + discountWithoutCOMMPart;

// Subtotal after Bottom line Discounts//
    subtotalPostQuoteDiscounts=subtotalPreQuoteDiscounts-bottomLineDiscountAmt;

// Total //
   total=subtotalPostQuoteDiscounts+ miscChargePostDiscount1_quote+miscChargePostDiscount2_quote+miscChargePostDiscount3_quote;

// Total Margins   
	if (subtotalPostQuoteDiscounts > 0.0 AND subtotalPostQuoteDiscounts > totalCost) {
		totalMarginPerc=((subtotalPostQuoteDiscounts-totalCost)/subtotalPostQuoteDiscounts)*100;
	}
	else { totalMarginPerc = 0.0;}

	totalMarginDollar=subtotalPostQuoteDiscounts-totalCost;

//Sell Price Subtotals NOTE: only used for Distributor processes
totalMiscItemsDistributors=(miscItem1Distributors_quote*miscItem1DistributorsQty_quote)+(miscItem2Distributors_quote*miscItem2DistributorsQty_quote)+ (miscItem3Distributors_quote*miscItem3DistributorsQty_quote)+ (miscItem4Distributors_quote*miscItem4DistributorsQty_quote); 	sellPriceSubtotalPostMiscItems=sellPriceSubtotal+totalMiscItemsDistributors;
	 
//Distributor Bottom Line Discounts	
    if(distributorBottomLineDiscountType_quote=="%"){
	    distributorBottomLineDiscount2=sellPriceSubtotalPostMiscItems*(distributorBottomLineDiscount_quote/100);
		distributorBottomLinediscountAmt=distributorBottomLineDiscount2; distributorBottomLineDiscountPer=distributorBottomLineDiscount_quote;}
    else{
	    distributorBottomLineDiscount2=(distributorBottomLineDiscount_quote/sellPriceSubtotalPostMiscItems)*100;
		distributorBottomLineDiscountAmt=distributorBottomLineDiscount_quote; distributorBottomLineDiscountPer=distributorBottomLineDiscount2;}
		
//Total Sell Discount
      totalSellDiscount=distributorBottomLineDiscountAmt+totalLineItemSellDiscount;

//Total Sell Price after Bottom Line Discounts
      	subtotalPostSellDiscount=sellPriceSubtotalPostMiscItems-distributorBottomLineDiscountAmt;
	  
//Total Sell Price for the Quote
    totalSellPrice=subtotalPostSellDiscount+miscChargePostDiscount1Distributor_quote+miscChargePostDiscount2Distributor_quote+ miscChargePostDiscount3Distributor_quote;
	
//Total Gross Profit for the Distributor
	if (totalSellPrice > 0.0) {
		totalGrossProfitPerc=((totalSellPrice-total)/totalSellPrice)*100;
	}
	else {totalGrossProfitPerc = 0.0;}
	totalGrossProfitDollar=totalSellPrice-total;

//Total Discount % - Kevin Deichl (Approval Discount Calculation)
totalDiscountPercentage = 0;	
	if (realTotalListPrice > 0.0) {
		totalDiscountPercentage = (realTotalDiscount / realTotalListPrice) * 100;
	}
	print realTotalDiscount ;
	print realTotalListPrice;
	totalDiscountPercentageWithoutCOMM = 0;
	if(totalListPriceWithoutCOMM > 0){
		totalDiscountPercentageWithoutCOMM = (totalDiscountWithoutCOMM/totalListPriceWithoutCOMM) *100;
	}


	// update for enabling discounts in Salesforce, case 114405, Dec 4, mjc
	//priceAdjustment_quote = totalDiscount_quote;  // using the totalDiscount_quote value to define the 
							// discount which goes to salesforce
	
// Build Results String //
res = res + _quote_process_document_number + "~discountWithoutCOMM_FBT_quote~" + string(totalDiscountPercentageWithoutCOMM) + "|";
res = res + _quote_process_document_number + "~totalCost_quote~" + string(totalCost) + "|";
res = res + _quote_process_document_number + "~totalListPrice_quote~" + string(totalListPrice) + "|";
res = res + _quote_process_document_number + "~totalLineItemDiscounts_quote~" + string(totalLineItemDiscounts) + "|";
res = res + _quote_process_document_number + "~totalSurcharge_quote~" + string(totalSurcharge) + "|";
res = res + _quote_process_document_number + "~totalNetPrice_quote~" + string(totalNetPrice) + "|";
res = res + _quote_process_document_number + "~totalLineItemSellDiscount_quote~" + string(totalLineItemSellDiscount) + "|";
res = res + _quote_process_document_number + "~subtotalPreQuoteDiscounts_quote~" + string(subtotalPreQuoteDiscounts) + "|";
res = res + _quote_process_document_number + "~bottomLineDiscount2_quote~" + string(bottomLineDiscount2) + bottomLineDiscount2Type+ "|";
res = res + _quote_process_document_number + "~totalDiscount_quote~" + string(totalDiscount) + "|";
res = res + _quote_process_document_number + "~totalMiscCharges_quote~" + string(totalMiscCharges) + "|";
res = res + _quote_process_document_number + "~subtotalPostQuoteDiscounts_quote~" + string(subtotalPostQuoteDiscounts) + "|";
res = res + _quote_process_document_number + "~total_quote~" + string(total) + "|";
res = res + _quote_process_document_number + "~totalMarginPerc_quote~" + string(totalMarginPerc) + "|";
res = res + _quote_process_document_number + "~totalMarginDollar_quote~" + string(totalMarginDollar) + "|";
res = res + _quote_process_document_number + "~sellPriceSubtotal_quote~" + string(sellPriceSubtotal) + "|";
res = res + _quote_process_document_number + "~totalMiscItemsDistributors_quote~" + string(totalMiscItemsDistributors) + "|";
res = res + _quote_process_document_number + "~sellPriceSubtotalPostMiscItems_quote~" + string(sellPriceSubtotalPostMiscItems) + "|";
res = res + _quote_process_document_number + "~distributorBottomLineDiscount2_quote~" + string(distributorBottomLineDiscount2) + distributorBottomLineDiscount2Type+ "|";
res = res + _quote_process_document_number + "~subtotalPostSellDiscount_quote~" + string(subtotalPostSellDiscount) + "|";
res = res + _quote_process_document_number + "~totalSellPrice_quote~" + string(totalSellPrice) + "|";
res = res + _quote_process_document_number + "~totalSellDiscount_quote~" + string(totalSellDiscount) + "|";
res = res + _quote_process_document_number + "~totalGrossProfitPerc_quote~" + string(totalGrossProfitPerc) + "|";
res = res + _quote_process_document_number + "~totalGrossProfitDollar_quote~" + string(totalGrossProfitDollar) + "|";

//Kevin Deichl
res = res + _quote_process_document_number + "~totalDiscount~" + string(totalDiscountPercentage) + "|";
//Aded for the Option line price with and without 
//res = res + _quote_process_document_number + "~optionalLineItemsTotal_FBT~" + string(optionalLine_Price) + "|";
//res = res + _quote_process_document_number + "~totalWithoutOptionalLineItems_FBT~" + string(withoutOptionalLine_Price) + "|";

// mjc
//res = res + _quote_process_document_number +"~priceAdjustment_quote~"+ string(priceAdjustment_quote) + "|";
// Return
return res+ret+sbtostring(returnSb);