import csv

FILE_TO_READ = "R_sG8WPrsWBcCfn1L_keypress_.csv"
row_to_headers_dict = {
    5: ["BINDING", "KEYPRESSTRIALTIMES", "GUESSTIME", "TONE"],
    7: ["BINDING", "DELAY", "CYCLE", "CYCLETIME", "TONE", "TONETIME",
        "KEYPRESSTRIALTIMES", "STARTTRIALTIMES", "ENDTRIALTIMES", "STARTTRIALAUDIOTIME", "DOTLOCATIONSTART", "DOTLOCATIONEND", "ISFULLSCREEN"],
    12: ["Binding", "keypressTrialTimes", "guessTime", "tone"],
    14: ["BINDING", "DELAY", "CYCLE", "CYCLETIME", "TONE", "TONETIME",
         "KEYPRESSTRIALTIMES", "STARTTRIALTIMES", "ENDTRIALTIMES", "STARTTRIALAUDIOTIME", "DOTLOCATIONSTART", "DOTLOCATIONEND", "ISFULLSCREEN"],
}


# consider a "header row" to be any row that doesn't contain information for a trial. These rows delineate between sections of trials.
def handle_header_row(row, num_header_rows_seen):
    if num_header_rows_seen in row_to_headers_dict.keys():
        return row_to_headers_dict[num_header_rows_seen]


def process_data_csv(FILE_TO_READ):
    file_lines_list = []
    with open(FILE_TO_READ) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        num_header_rows_seen = 0
        for row in csv_reader:
            if row[0]:
                if row[0][:5] != "trial":  # Not a "trial" row, switch it up
                    num_header_rows_seen += 1
                    row_rep = handle_header_row(row, num_header_rows_seen)
                    if row_rep:
                        row = row_rep

            file_lines_list.append(row)
    return file_lines_list


def write_out_csv(lines):
    with open("output.csv", 'w') as csvfile:
        csvwriter = csv.writer(csvfile)
        csvwriter.writerows(lines)


if __name__ == '__main__':
    file_lines_list = process_data_csv(FILE_TO_READ)
    write_out_csv(file_lines_list)
    print("done")
