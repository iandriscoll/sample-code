library(XML)
library(stringr)
library(ggplot2)

read_archive = function(x) {
  url = paste0("http://cran.r-project.org/src/contrib/Archive/", x)
  readHTMLTable(url)
}

version_sizes = function(x) {
  x = as.character(x)
  y = c(rep(0, length(x)))
  for (i in 1:length(x)) {
    if (!is.na(str_extract(x[i], "K"))) {
      y[i] = as.numeric(substr(x[i], 1, nchar(x[i]) - 1))
    } else {
      y[i] = as.numeric(substr(x[i], 1, nchar(x[i]) - 1)) * 1000
    }
  }
  y
}



clean_archive = function(x) {
  x = as.data.frame(x)[]
  y = x[-c(1, 2, (nrow(x))), ]
  
  a = data.frame(
    name = str_extract(y[[2]], "[a-z]+"),
    version = str_extract(y[[2]], "[0-9]+.[0-9]{1,2}+.[0-9]{1,2}|[0-9]+.[0-9]{1,2}"),
    date = as.Date(str_extract(y[[3]], "[0-9]{4}+-[0-9]{2}+-[0-9]{2}")),
    size =  version_sizes(y[[4]])
    )
    a
}
raw_data = read_archive('stringr')
clean_data = clean_archive(raw_data)
write.csv(clean_data, "../data/stringr-archive.csv")

plot_archive = function(archive) {
  return(ggplot() +
           geom_step(aes(x = archive$date, y = archive$size)) +
           ggtitle(paste0(archive$name[1], ": timeline of version sizes")))
}

plot_archive(clean_data)
