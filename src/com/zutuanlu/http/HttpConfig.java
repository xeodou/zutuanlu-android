package com.zutuanlu.http;

public class HttpConfig {
	//net state
	public static final int TYPE_NET_WORK_DISABLED = 0; //缃戣矾涓嶅彲鐢�
	public static final int TYPE_MNO_CM = 1;//绉诲姩缃戣矾
	public static final int TYPE_MNO_CU = 2;//鑱旈�缃戠粶
	public static final int TYPE_MNO_CT = 3;//鐢典俊缃戠粶
	public static final int TYPE_CM_CU_WAP = 4;//绉诲姩鑱旈�wap
	public static final int TYPE_CT_WAP = 5;//鐢典俊wap
	public static final int TYPE_OTHER_NET = 6;//绉诲姩鑱旈�鐢典俊NET缃戠粶
	public static final int TYPE_WIFI = 7;//wifi缃戠粶
	public static int CURRENT_NET_WORK_TYPE = TYPE_OTHER_NET;//
	
	//csrf
	public static String _CSRF;
}
