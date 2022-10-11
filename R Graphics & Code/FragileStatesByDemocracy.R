rm(list = ls())
setwd("set this to some destination")
getwd()

library("tidyverse")
library("readxl")

fsi <- read_xlsx("fragileStatesIndex.xlsx")
fsi <- fsi[c(1, 4)]
colnames(fsi) <- c("Country", "FragileStatesIndex")
dmcrcyMtrx <- read_csv("democracyIndex.csv")

newDM <- dmcrcyMtrx[121,]

for (x in 122:nrow(dmcrcyMtrx)) {
  dmcrcyRow <- dmcrcyMtrx[x,]
  if (dmcrcyRow[1,2] == 2020) {
    newDM[nrow(newDM) + 1,] <- dmcrcyRow
  }
}

newDM <- newDM[, c(1, 27)]
newDM <- na.omit(newDM)

colnames(newDM) <- c("Country", "DemocracyIndex")

finalData <- merge(fsi, newDM, by = "Country")

ggplot(finalData, aes(x = DemocracyIndex, y = FragileStatesIndex)) + 
  geom_point(data = finalData[1:20,], alpha = 0.6) + 
  geom_point(data = finalData[21,], alpha = 0.6, size = 3) + 
  annotate("text", x = finalData[21,3], y = finalData[21,2] + 3.5, 
           label = "Burkina Faso", size = 3) +
  geom_point(data = finalData[22:114,], alpha = 0.6) +
  geom_point(data = finalData[115,], alpha = 0.6, size = 3) + 
  annotate("text", x = finalData[115,3], y = finalData[115,2] + 3.5, 
           label = "Qatar", size = 3) +
  geom_point(data = finalData[116:123,], alpha = 0.6) + 
  geom_point(data = finalData[124,], alpha = 0.6, size = 3) + 
  annotate("text", x = finalData[124,3], y = finalData[124,2] - 3,
           label = "Singapore", size = 3) + 
  geom_point(data = finalData[125:149,], alpha = 0.6) +
  geom_point(data = finalData[150,], size = 3, alpha = 0.6) + 
  annotate("text", x = finalData[150,3], y = finalData[150,2] - 3.3,
           label = "United Arab Emirates", size = 3) + 
  geom_point(data = finalData[151:154,], alpha = 0.6) + 
  geom_point(data = finalData[155,], alpha = 0.6, size = 3) + 
  annotate("text", x = finalData[155,3], y = finalData[155,2] + 3.5, 
           label = "Yemen", size = 3) + 
  stat_smooth(method = 'lm', formula = y ~ x + I(x^2), size = 1) + 
  labs(y = "Fragile States Index: Total Rating (2022)", 
       x = "Democracy Matrix: Total Rating (2020, Most Up-to-Date)", 
       title = "State Stability Compared to Overall Domestic Freedom", 
       subtitle = "Fragile States Index rating plotted by Democracy Matrix Rating for 176 countries", 
       caption = "By Andrew Cupps\nSources:\nDemocracy Matrix (democracymatrix.com)\nFragile States Index (fragilestatesindex.org)") +
  theme(plot.title = element_text(face = "bold", size = 16), 
        plot.caption = element_text(face = "italic"), 
        axis.line = element_line(colour = "black", size = 0.5), 
        panel.grid.major = element_line(colour = "black", size = 0.1), 
        panel.grid.minor = element_line(colour = "black", size = 0.1, linetype = "dashed"), 
        panel.background = element_rect(fill = "gray95"))

ggsave("fragileStatesByDemocracy.png", dpi = 300)

#Burkina Faso: 21
#Singapore: 124
#Qatar: 115
#Yemen: 155
#UAE: 150
