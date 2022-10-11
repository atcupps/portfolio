rm(list = ls())
setwd("/Users/andrewcupps/Documents/HGLO101/datasets")
getwd()

library("tidyverse")
library("ggthemes")

rawData <- read_csv("incomePerCapita.csv")
data1 <- rawData[c(1:5), c(3, 5:32)]
colnames(data1) <- c("Region", 1993:2020)
finalData <- data1 %>% 
  pivot_longer(c(2:29), names_to = "Year", values_to = "IncomePP")

col1 <- finalData[,2]
col1 <- as.numeric(unlist(col1))
finalData[,2] <- col1

colors <- c("#ff6257", "#009c65", "#b89000", "#08009c", "#b80000")

ggplot(finalData, aes(x = Year, y = IncomePP, linetype = Region, colour = Region)) + 
  geom_line() + 
  labs(x = "Year", y = "Adjusted Net Income per Capita (current USD)", 
       title = "Rising Average Incomes Across Non-Western Regions",
       subtitle = "Income per Capita by Year, 1993 - 2020", 
       caption = "By Andrew Cupps\nSource: WorldBank (NY.ADJ.NNTY.PC.CD)") +
  scale_color_manual(values = colors) + 
  theme(plot.title = element_text(face = "bold", size = 16), 
        #plot.caption = element_text(face = "italic", hjust = 1.8), 
        axis.line.x = element_line(colour = "black", size = 0.5), 
        axis.line.y = element_line(colour = "black", size = 0.5), 
        panel.grid.major = element_line(colour = "black", size = 0.1), 
        panel.grid.minor = element_line(colour = "black", linetype = "dashed", size = 0.1),
        panel.background = element_rect(fill = "white"), 
        legend.title = element_text(face = "bold"),
        legend.background = element_rect(fill = "gray95", colour = "black"))

ggsave("incomePPbyYearbyRegion.png", dpi = 300)
