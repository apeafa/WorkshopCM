# WorkshopCM - FACEBOOK SDK + ANDROID
Workshop

Esta aplicação foi criada no intuito de mostrar pequenos pedaços de código exemplo para um workshop de Computação Móvel, no dia 16/11/2015

#----------------------------------------------#
#        CÓDIGO ÚTIL PARA WORKSHOP             #
#----------------------------------------------#

#------------------1---------------------------#
#           GET MY KEY HASH                    #
#----------------------------------------------#

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey();
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "PACKAGE_NAME",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}

#------------------------------------------fim get my key hash--------------------------------------------#
