# -----------------------------------------------------------------------------
# Title: Shot charts script
# Description: Creating shot charts for each Warriors player
# Input(s): nba-court.jpg, shots-data.csv
# Output(s): andre-iguodala-shot-chart.pdf, draymond-green-shot-chart.pdf,
#           gsw-shot-charts.pdf, kevin-durant-shot-chart.pdf
#           klay-thompson-shot-chart.pdf, stephen-curry-shot-chart.pdf
# -----------------------------------------------------------------------------

library(grid)
library(jpeg)
library(ggplot2)

# getting court image

court_file = '../images/nba-court.jpg'
court_image = rasterGrob(
  readJPEG(court_file),
  width = unit(1, 'npc'),
  height = unit(1, 'npc')
)

# loading data sets of each player

dat = read.csv('../data/shots-data.csv')

# making each of the shot charts

iguodala_shot_chart = ggplot(data = dat[dat$name == 'Andre Iguodala',]) + 
  annotation_custom(court_image, -250, 250, -50, 420) + 
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) + 
  ggtitle('Shot Chart for Andre Iguodala (2016 season)') + 
  theme_classic()
iguodala_shot_chart

green_shot_chart = ggplot(data = dat[dat$name == 'Draymond Green',]) + 
  annotation_custom(court_image, -250, 250, -50, 420) + 
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) + 
  ggtitle('Shot Chart for Draymond Green (2016 season)') + 
  theme_classic()
green_shot_chart

durant_shot_chart = ggplot(data = dat[dat$name == 'Kevin Durant',]) + 
  annotation_custom(court_image, -250, 250, -50, 420) + 
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) + 
  ggtitle('Shot Chart for Kevin Durant (2016 season)') + 
  theme_classic()
durant_shot_chart

thompson_shot_chart = ggplot(data = dat[dat$name == 'Klay Thompson',]) + 
  annotation_custom(court_image, -250, 250, -50, 420) + 
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) + 
  ggtitle('Shot Chart for Klay Thompson (2016 season)') + 
  theme_classic()
thompson_shot_chart

curry_shot_chart = ggplot(data = dat[dat$name == 'Stephen Curry',]) + 
  annotation_custom(court_image, -250, 250, -50, 420) + 
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) + 
  ggtitle('Shot Chart for Stephen Curry (2016 season') + 
  theme_classic()
curry_shot_chart

# exporting PDFs

pdf('../images/andre-iguodala-shot-chart.pdf', width = 6.5, height = 5)
iguodala_shot_chart
dev.off()

pdf('../images/draymond-green-shot-chart.pdf', width = 6.5, height = 5)
green_shot_chart
dev.off()

pdf('../images/kevin-durant-shot-chart.pdf', width = 6.5, height = 5)
durant_shot_chart
dev.off()

pdf('../images/klay-thompson-shot-chart.pdf', width = 6.5, height = 5)
thompson_shot_chart
dev.off()

pdf('../images/stephen-curry-shot-chart.pdf', width = 6.5, height = 5)
curry_shot_chart
dev.off()


# faceting by player

gsw_shot_charts = ggplot(data = dat) + 
  annotation_custom(court_image, -250, 250, -50, 420) + 
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) + 
  ggtitle('Warriors Players Shot Charts (2016 season)') + 
  theme_classic() + facet_wrap( ~ name)
gsw_shot_charts


# exporting PDF

pdf('../images/gsw-shot-charts.pdf', width = 8, height = 7)
gsw_shot_charts
dev.off()

