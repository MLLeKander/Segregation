function s=plotMetrics(data)
    function nums = readNums(chars)
        nums = str2num(strjoin(cellstr(chars),','));
    end
    function [] = plotHist(data1, data2, binwidth)
        dataMin = min([data1 data2]); dataMax = max([data1 data2]);
        h1 = histogram(data1, 'BinLimits', [dataMin dataMax], 'BinWidth', binwidth, 'Normalization', 'probability');%, bins);
        hold on;
        h2 = histogram(data2, 'BinLimits', [dataMin dataMax], 'BinWidth', binwidth, 'Normalization', 'probability');%, bins);
    end
    function [] = plotHistInt(data1, data2)
        [cnts1, edges1] = histcounts(data1, 'BinMethod', 'integers');
        [cnts2, edges2] = histcounts(data2, 'BinMethod', 'integers');
        cnts1 = cnts1.*(edges1(1:end-1)+.5);
        cnts2 = cnts2.*(edges2(1:end-1)+.5);
        sum(cnts1) == sum(cnts2)
        bar(cnts1,1,'FaceColor', [0    0.4470    0.7410], 'FaceAlpha', 0.6);
        hold on;
        bar(cnts2,1,'FaceColor', [0.8500    0.3250    0.0980], 'FaceAlpha', 0.6);
    end
    if isstr(data)
        s = tdfread(data);
    else
        s = data;
    end
    % numFeatures seed  similarity  emptiness   rows  cols  strat threshes numEpochs   beforeSimilarity  beforeMooreClustering   beforeNeumannClustering afterSimilarity   afterMooreClustering afterNeumannClustering
    if ~isfield(s,'beforeSimilarityNums')
        s.beforeSimilarityNums=readNums(s.beforeSimilarity);
        s.beforeMooreClusteringNums=readNums(s.beforeMooreClustering);
        s.beforeNeumannClusteringNums=readNums(s.beforeNeumannClustering);
        s.afterSimilarityNums=readNums(s.afterSimilarity);
        s.afterMooreClusteringNums=readNums(s.afterMooreClustering);
        s.afterNeumannClusteringNums=readNums(s.afterNeumannClustering);
    end
    
    figure(1); clf;
    plotHist(s.beforeSimilarityNums, s.afterSimilarityNums, 0.1);
    xlabel('Similarity score');
    ylabel('Counts');
    title(sprintf('Similarity scores\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',s.numFeatures(1),s.emptiness(1)*100,s.cols(1),s.rows(1),s.strat(1,:)));
    legend('Initial similarity scores','Final similarity scores','Location','northwest');
    meanBeforeSimilarity=mean(s.beforeSimilarityNums);
    meanAfterSimilarity=mean(s.afterSimilarityNums);
    maxsim=max([hist(s.beforeSimilarityNums) hist(s.afterSimilarityNums)]);
    %text(5,(maxsim+2),strcat('Initial mean = ',meanBeforeSimilarity,', final mean = ',meanAfterSimilarity));
    saveas(gcf,'Similarity.png');
    %clf;
    
    figure(2); clf;
    plotHistInt(s.beforeMooreClusteringNums,s.afterMooreClusteringNums);
    xlabel('Moore Cluster Size');
    ylabel('Counts');
    title(sprintf('Moore cluster sizes\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',s.numFeatures(1),s.emptiness(1)*100,s.cols(1),s.rows(1),s.strat(1,:)));
    legend('Initial Moore cluster sizes','Final Moore cluster sizes');
    meanbmoore=mean(s.beforeMooreClusteringNums);
    meanamoore=mean(s.afterMooreClusteringNums);
    maxmoore=max([hist(s.beforeMooreClusteringNums) hist(s.afterMooreClusteringNums)]);
    %text(5,(maxmoore+2),strcat('Mean after 0 epochs = ',meanbmoore,', mean after ',epochs,' epochs = ',meanamoore));
    saveas(gcf,'Moore.png');
    %clf;
       
    figure(3); clf;
    plotHistInt(s.beforeNeumannClusteringNums,s.afterNeumannClusteringNums);
    xlabel('von Neumann Cluster Size');
    ylabel('Counts');
    title(sprintf('von Neumann cluster sizes\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',s.numFeatures(1),s.emptiness(1)*100,s.cols(1),s.rows(1),s.strat(1,:)));
    legend('Initial von Neumann cluster sizes','Final von Neumann cluster sizes');
    meanbneumann=mean(s.beforeNeumannClusteringNums);
    meananeumann=mean(s.afterNeumannClusteringNums);
    maxneumann=max([hist(s.beforeNeumannClusteringNums) hist(s.afterNeumannClusteringNums)]);
    %text(5,(maxneumann+2),strcat('Mean after 0 epochs = ',meanbneumann,', mean after ',epochs,' epochs = ',meananeumann));
    saveas(gcf,'Neumann.png');
    %clf;
end
