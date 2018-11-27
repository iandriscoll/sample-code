# -----------------------------------------------------------------------------
# Title: Shots Data
# Description: Compiling the 5 data
# Input(s): andre-iguodala.csv, draymond-green.csv, kevin-durant.csv,
#           klay-thompson.csv, stephen-curry.csv
# Output(s): andre-iguodala-summary.txt, draymond-green-summary.txt,
#           kevin-durant-summary.txt, klay-thompson-summary.txt, 
#           shots-data-summary.txt, stephen-curry-summary.txt
# -----------------------------------------------------------------------------

# Reading in CSVs

iguodala = read.csv('../data/andre-iguodala.csv', na = 'NA', colClasses = c(season = 'factor', shot_made_flag = 'character'))
green = read.csv('../data/draymond-green.csv', na = 'NA', colClasses = c(season = 'factor', shot_made_flag = 'character'))
durant = read.csv('../data/kevin-durant.csv', na = 'NA', colClasses = c(season = 'factor', shot_made_flag = 'character'))
thompson = read.csv('../data/klay-thompson.csv', na = 'NA', colClasses = c(season = 'factor', shot_made_flag = 'character'))
curry = read.csv('../data/stephen-curry.csv', na = 'NA', colClasses = c(season = 'factor', shot_made_flag = 'character'))

# Adding name to each frame

iguodala$name = 'Andre Iguodala'
green$name = 'Draymond Green'
durant$name = 'Kevin Durant'
thompson$name = 'Klay Thompson'
curry$name = 'Stephen Curry'

# Changing iguodala shot_made_flag

iguodala[iguodala == 'y'] = 'made shot'
iguodala[iguodala == 'n'] = 'missed shot'

# Changing green shot_made_flag

green[green == 'y'] = 'made shot'
green[green == 'n'] = 'missed shot'

# Changing durant shot_made_flag

durant[durant == 'y'] = 'made shot'
durant[durant == 'n'] = 'missed shot'

# Changing thompson shot_made_flag

thompson[thompson == 'y'] = 'made shot'
thompson[thompson == 'n'] = 'missed shot'

# Changing curry shot_made_flag

curry[curry == 'y'] = 'made shot'
curry[curry == 'n'] = 'missed shot'

# adding minute column

iguodala$minute = 12 * (iguodala$period - 1) + (12 - iguodala$minutes_remaining)
green$minute = 12 * (green$period - 1) + (12 - green$minutes_remaining)
durant$minute = 12 * (durant$period - 1) + (12 - durant$minutes_remaining)
thompson$minute = 12 * (thompson$period - 1) + (12 - thompson$minutes_remaining)
curry$minute = 12 * (curry$period - 1) + (12 - curry$minutes_remaining)

# sinking summaries

sink('../output/andre-iguodala-summary.txt')
summary(iguodala)
sink()
sink('../output/draymond-green-summary.txt')
summary(green)
sink()
sink('../output/kevin-durant-summary.txt')
summary(durant)
sink()
sink('../output/klay-thompson-summary.txt')
summary(thompson)
sink()
sink('../output/stephen-curry-summary.txt')
summary(curry)
sink()

# binding data frames together and writing to folder

shots_data = rbind(iguodala, green, durant, thompson, curry)
write.csv(shots_data, '../data/shots-data.csv')
sink('../output/shots-data-summary.txt')
summary(shots_data)
sink()
