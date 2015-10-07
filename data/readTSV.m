function s = readFile(filename)
    file = fopen(filename);
    expectedHeader = sprintf('numFeatures\tseed\tsimilarity\temptiness\trows\tcols\tstrat\tthreshes\tnumEpochs\tbeforeSimilarity\tbeforeMooreClustering\tbeforeNeumannClustering\tafterSimilarity\tafterMooreClustering\tafterNeumannClustering');
    actualHeader = fgetl(file);
    assert(strcmp(actualHeader, expectedHeader), 'Unknown data file format');
    s = readLine(file);
    while ~feof(file)
       s(end+1) = readLine(file);
    end
    
    function s = readLine(file, s, ndx)
        s = struct();
        a = num2cell(fscanf(file, '%d\t%d\t%f\t%f\t%d\t%d\t'));
        [s.numFeatures, s.seed, s.similarity, s.emptiness, s.rows, s.cols] = deal(a{:});
        s.strat = fscanf(file, '%s\t', 1);
        s.threshes = fscanf(file, '%f,');
        s.numEpochs = fscanf(file, '%d\t',1);
        s.beforeSimilarity = fscanf(file, '%f,'); fscanf(file, '\t');
        s.beforeMooreClustering = fscanf(file, '%f,'); fscanf(file, '\t');
        s.beforeNeumannClustering = fscanf(file, '%f,'); fscanf(file, '\t');
        s.afterSimilarity = fscanf(file, '%f,'); fscanf(file, '\t');
        s.afterMooreClustering = fscanf(file, '%f,'); fscanf(file, '\t');
        s.afterNeumannClustering = fscanf(file, '%f,');
        fgetl(file);
    end
    fclose(file);
end
