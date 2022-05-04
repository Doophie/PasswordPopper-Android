package ca.doophie.passwordpopper.extensions


fun String.getIconURL(): String {
    return "https://www.google.com/s2/favicons?sz=64&domain_url=$this"
}